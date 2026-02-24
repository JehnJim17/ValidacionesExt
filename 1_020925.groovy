# ============================================================
# JSM Cloud - Parseo de change log y cálculo de tiempos
# Autoría: preparado para Google Colab (Jennifer A. Jiménez)
# ============================================================

# --- 0) Dependencias ---
import re
import io
import json
import math
from datetime import datetime, timedelta, time, date
from zoneinfo import ZoneInfo
import pandas as pd

# Instalar xlsxwriter si no está presente
try:
    import xlsxwriter
except ImportError:
    print("Instalando xlsxwriter...")
    !pip install xlsxwriter
    import xlsxwriter

try:
    from google.colab import files  # disponible en Colab
    IN_COLAB = True
except Exception:
    IN_COLAB = False

# --- 1) Parámetros de calendario / formato ---
TZ = ZoneInfo("America/Bogota")

# Jornada laboral por día hábil
WORK_SESSIONS = [
    (time(8, 0),  time(12, 0)),
    (time(13, 30), time(17, 30)),
]

# ¿Aplicar festivos SOLO del año_base a los cómputos laborales?
APLICAR_FESTIVOS_SOLO_EN_ANIO_BASE = True
ANIO_BASE_FESTIVOS = 2025  # ajústalo si deseas otro año base

# Festivos Colombia 2025 (observados/Ley Emiliani)
FESTIVOS_CO_2025 = {
    date(2025,1,1),   # Año Nuevo
    date(2025,1,6),   # Reyes Magos (Ley Emiliani)
    date(2025,3,24),  # San José (Ley Emiliani)
    date(2025,4,17),  # Jueves Santo
    date(2025,4,18),  # Viernes Santo
    date(2025,5,1),   # Día del Trabajo
    date(2025,6,2),   # Ascensión (Ley Emiliani)
    date(2025,6,23),  # Corpus Christi (Ley Emiliani)
    date(2025,6,30),  # Sagrado Corazón (y San Pedro/San Pablo observado)
    date(2025,7,20),  # Independencia (domingo; se incluye para referencia)
    date(2025,8,7),   # Batalla de Boyacá
    date(2025,8,18),  # Asunción (Ley Emiliani)
    date(2025,10,13), # Día de la Raza (Ley Emiliani)
    date(2025,11,3),  # Todos los Santos (Ley Emiliani)
    date(2025,11,17), # Independencia de Cartagena (Ley Emiliani)
    date(2025,12,8),  # Inmaculada Concepción
    date(2025,12,25), # Navidad
}

# Formato de salida
# - En laboral, 1d = 8h (día hábil)
# - En 24h,    1d = 24h
LABOR_DAY_HOURS = 8
FULL_DAY_HOURS  = 24


# --- 2) Utilidades de tiempo ---
def parse_dt(s: str) -> datetime:
    """
    Intenta parsear timestamps tipo:
      2026-01-26T17:19:14.218-0500  o
      2026-01-26T17:19:14-0500
    """
    for fmt in ("%Y-%m-%dT%H:%M:%S.%f%z", "%Y-%m-%dT%H:%M:%S%z"):
        try:
            return datetime.strptime(s, fmt).astimezone(TZ)
        except ValueError:
            continue
    # Último recurso: quitar milisegundos si vienen raros
    s2 = re.sub(r"\\.(\\d+)", "", s)
    for fmt in ("%Y-%m-%dT%H:%M:%S%z",):
        try:
            return datetime.strptime(s2, fmt).astimezone(TZ)
        except ValueError:
            continue
    raise ValueError(f"No pude interpretar la fecha: {s}")


def is_business_day(d: date) -> bool:
    # Fin de semana
    if d.weekday() >= 5:  # 5=Sab, 6=Dom
        return False
    # Festivos 2025 (si aplica)
    if APLICAR_FESTIVOS_SOLO_EN_ANIO_BASE and d.year == ANIO_BASE_FESTIVOS:
        if d in FESTIVOS_CO_2025:
            return False
    return True


def business_seconds_between(s: datetime, e: datetime) -> int:
    """
    Intersección (s, e) con las sesiones laborales definidas
    considerando L–V y festivos (según regla).
    """
    if s is None or e is None or e <= s:
        return 0
    s = s.astimezone(TZ)
    e = e.astimezone(TZ)
    total = 0
    cur = s.date()
    last = e.date()
    one_day = timedelta(days=1)

    while cur <= last:
        if is_business_day(cur):
            for ws_start, ws_end in WORK_SESSIONS:
                ws_s_dt = datetime.combine(cur, ws_start, tzinfo=TZ)
                ws_e_dt = datetime.combine(cur, ws_end, tzinfo=TZ)
                a = max(ws_s_dt, s)
                b = min(ws_e_dt, e)
                if b > a:
                    total += int((b - a).total_seconds())
        cur += one_day
    return total


def fmt_dhm_from_seconds(seconds: int, day_hours: int) -> str:
    """
    Convierte segundos a 'Xd Yh Zm'
    - day_hours = 8 para laboral
    - day_hours = 24 para 24h
    """
    if seconds is None:
        return "N/D"
    total_minutes = seconds // 60
    minutes = total_minutes % 60
    total_hours = total_minutes // 60
    days = total_hours // day_hours
    hours = total_hours % day_hours
    return f"{days}d {hours}h {minutes}m"


# --- 3) Carga de archivo ---
def cargar_changelog_desde_colab() -> str:
    if not IN_COLAB:
        raise RuntimeError("Este helper solo funciona en Google Colab.")
    print("🔼 Selecciona tu archivo de change log (por ejemplo, Changelog.md):")
    up = files.upload()  # abre el diálogo
    if not up:
        raise RuntimeError("No se subió ningún archivo.")
    nombre = list(up.keys())[0]
    return nombre

def leer_texto(filename: str) -> str:
    with io.open(filename, "r", encoding="utf-8", errors="ignore") as f:
        return f.read()


# --- 4) Parser robusto de transiciones de estado ---
def extraer_transiciones_status(raw_text: str):
    """
    Escanea línea a línea:
      - Captura cada 'created' de un bloque de history
      - En cada bloque, por cada item con fieldId == 'status', recoge fromString y toString
    Devuelve lista de dicts: {created_dt, from, to}
    """
    lines = raw_text.splitlines()
    trans = []
    current_created = None

    # Pre-capturamos TODOS los created (para encontrar el earliest)
    all_created = []
    for line in lines:
        if '"created":' in line:
            m = re.search(r'"created"\s*:\s*"([0-9T:\-\\.+:]*)"', line)
            if m:
                all_created.append(m.group(1))

    # Recorremos nuevamente para casar created -> status
    for i, line in enumerate(lines):
        if '"created":' in line:
            m = re.search(r'"created"\s*:\s*"([0-9T:\-\\.+:]*)"', line)
            if m:
                current_created = m.group(1)

        if '"fieldId": "status"' in line:
            # Miramos una ventana siguiente para atrapar fromString/toString
            window = "\n".join(lines[i:i+40])
            from_m = re.search(r'"fromString"\s*:\s*"(.*?)"', window)
            to_m   = re.search(r'"toString"\s*:\s*"(.*?)"', window)
            if current_created and to_m:
                try:
                    cdt = parse_dt(current_created)
                except Exception:
                    continue
                trans.append({
                    "created_dt": cdt,
                    "from": from_m.group(1) if from_m else None,
                    "to":   to_m.group(1)
                })

    # Orden cronológico
    trans.sort(key=lambda x: x["created_dt"])

    # Determinamos el earliest_dt del historial para “inicio” del estado inicial
    earliest_dt = None
    if all_created:
        try:
            earliest_dt = min(parse_dt(s) for s in all_created)
        except Exception:
            earliest_dt = trans[0]["created_dt"] if trans else None

    return trans, earliest_dt


# --- 5) Construcción de intervalos de estado (sin omitir NINGUNO) ---
def construir_intervalos(trans, earliest_dt):
    """
    Genera tramos {estado, start, end}:
      - Primer tramo: desde earliest_dt hasta el primer cambio de status.
        El nombre del estado inicial será:
           1) trans[0]['from'] si existe, o
           2) 'Solicitud Creada' si aparece en algún 'from'/'to' del log, o
           3) '(Estado inicial desconocido)' en su defecto.
      - Para cada transición: tramo del estado 'to' hasta la próxima marca.
      - Incluye TODOS los estados que aparecen en 'from' o 'to'.
    """
    intervals = []
    if not trans:
        return intervals

    # Candidato de nombre inicial
    nombre_inicial = trans[0]["from"]
    if not nombre_inicial:
        # Buscamos si 'Solicitud Creada' aparece en algún lado como pista
        todos_estados = set()
        for t in trans:
            if t["from"]:
                todos_estados.add(t["from"])
            if t["to"]:
                todos_estados.add(t["to"])
        if "Solicitud Creada" in todos_estados:
            nombre_inicial = "Solicitud Creada"
        else:
            nombre_inicial = "(Estado inicial desconocido)"

    # Primer tramo (estado inicial) si tenemos earliest_dt
    if earliest_dt:
        intervals.append({
            "state": nombre_inicial,
            "start": earliest_dt,
            "end":   trans[0]["created_dt"]
        })

    # Tramos siguientes
    for i, t in enumerate(trans):
        start = t["created_dt"]
        end   = trans[i+1]["created_dt"] if (i+1) < len(trans) else None
        intervals.append({
            "state": t["to"],
            "start": start,
            "end":   end
        })

    return intervals


# --- 6) Construcción de tablas (detalle y resumen) ---
def construir_tablas(intervals):
    detalle_rows = []
    for it in intervals:
        st = it["start"]
        en = it["end"]
        dur_24s  = int((en - st).total_seconds()) if st and en else None
        dur_bizs = business_seconds_between(st, en) if st and en else None

        detalle_rows.append({
            "Estado": it["state"],
            "Inicio": st.strftime("%Y-%m-%d %H:%M:%S") if st else "N/D",
            "Fin":    en.strftime("%Y-%m-%d %H:%M:%S") if en else "N/D",
            "Tiempo laboral": fmt_dhm_from_seconds(dur_bizs, LABOR_DAY_HOURS) if dur_bizs is not None else "N/D",
            "Tiempo 24h":     fmt_dhm_from_seconds(dur_24s,  FULL_DAY_HOURS)  if dur_24s  is not None else "N/D",
            "_biz_seconds": dur_bizs if dur_bizs is not None else 0,
            "_24h_seconds":  dur_24s  if dur_24s  is not None else 0,
        })

    # DataFrame detalle
    df_detalle = pd.DataFrame(detalle_rows)[
        ["Estado", "Inicio", "Fin", "Tiempo laboral", "Tiempo 24h"]
    ]

    # Resumen por estado (suma de todos sus tramos)
    resumen = (
        pd.DataFrame(detalle_rows)
        .groupby("Estado", as_index=False)[["_biz_seconds", "_24h_seconds"]].sum()
    )

    resumen["Tiempo laboral (sum)"] = resumen["_biz_seconds"].apply(lambda s: fmt_dhm_from_seconds(int(s), LABOR_DAY_HOURS))
    resumen["Tiempo 24h (sum)"]     = resumen["_24h_seconds"].apply(lambda s: fmt_dhm_from_seconds(int(s), FULL_DAY_HOURS))

    # TOTAL al final
    total_row = {
        "Estado": "TOTAL",
        "_biz_seconds": int(resumen["_biz_seconds"].sum()),
        "_24h_seconds": int(resumen["_24h_seconds"].sum()),
    }
    total_row["Tiempo laboral (sum)"] = fmt_dhm_from_seconds(total_row["_biz_seconds"], LABOR_DAY_HOURS)
    total_row["Tiempo 24h (sum)"]     = fmt_dhm_from_seconds(total_row["_24h_seconds"], FULL_DAY_HOURS)

    df_resumen = resumen[["Estado", "Tiempo laboral (sum)", "Tiempo 24h (sum)"]]
    df_resumen = pd.concat([df_resumen, pd.DataFrame([{
        "Estado": "TOTAL",
        "Tiempo laboral (sum)": total_row["Tiempo laboral (sum)"],
        "Tiempo 24h (sum)":     total_row["Tiempo 24h (sum)"],
    }])], ignore_index=True)

    return df_detalle, df_resumen


# --- 7) Main: cargar, parsear, exportar ---
def main(nombre_archivo: str = None):
    if nombre_archivo is None and IN_COLAB:
        nombre_archivo = cargar_changelog_desde_colab()
    elif nombre_archivo is None:
        raise RuntimeError("Proporciona el nombre del archivo cuando no estás en Colab.")

    raw = leer_texto(nombre_archivo)
    trans, earliest_dt = extraer_transiciones_status(raw)

    if not trans:
        raise RuntimeError(
            "No encontré transiciones de 'status' en el archivo. "
            "Verifica el contenido (debe incluir 'fieldId\": \"status\")."
        )

    intervals = construir_intervalos(trans, earliest_dt)
    df_detalle, df_resumen = construir_tablas(intervals)

    # Exportar un solo Excel con dos hojas
    excel_filename = "estados_consolidados.xlsx"
    with pd.ExcelWriter(excel_filename, engine='xlsxwriter') as writer:
        df_detalle.to_excel(writer, sheet_name='Detalle de Estados', index=False)
        df_resumen.to_excel(writer, sheet_name='Resumen de Estados', index=False)

    print("✅ Listo. Archivos generados:")
    print(" - estados_consolidados.xlsx (Detalle y resumen en un solo archivo Excel)")

    # Descargar archivos
    if IN_COLAB:
        files.download(excel_filename)

    display(df_detalle.head(15))
    display(df_resumen.head(20))

main()
