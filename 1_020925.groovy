# ============================================================
# 1) Setup (solo estándar de Colab; no requiere instalaciones)
# ============================================================
import re
import pandas as pd

# Si deseas subir el archivo desde tu equipo, descomenta esto:
# from google.colab import files
# uploaded = files.upload()  # Selecciona el CSV original (separado por ';')

# Nombre del archivo de entrada (ajústalo si subes otro nombre)
input_path = 'jira-survey-feedback-table (4).csv'  # <-- cambia si corresponde

# ============================================================
# 2) Cargar CSV original (separado por ';')
# ============================================================
df = pd.read_csv(input_path, sep=';', dtype=str, encoding='utf-8')
df.columns = [c.strip() for c in df.columns]

# Algunas exportaciones traen una fila residual "sep=" al final: la elimino
if 'Ticket-id' in df.columns:
    df = df[df['Ticket-id'].astype(str).str.strip() != 'sep=']

# ============================================================
# 3) Definiciones de preguntas y limpieza
# ============================================================
col_qa = 'Questions/Answers'

Q1 = 'Resolver este requerimiento a través de Verona fue fácil.'
Q2 = 'El tiempo de atención de tu requerimiento fue adecuado.'
Q3 = 'El equipo del CSA entendió tu necesidad y te acompañó adecuadamente durante el proceso.'
Q4 = '¿Qué podríamos mejorar para que tu experiencia en próximos requerimientos sea mejor?'

scale_legend = '1️⃣ Totalmente en desacuerdo → 5️⃣ Totalmente de acuerdo:'

def limpiar_y_extraer(cell: str):
    """Deja Q1..Q4 mapeadas a sus respuestas en texto (sin 'Escala:' ni leyenda)."""
    if pd.isna(cell):
        return {Q1: None, Q2: None, Q3: None, Q4: None}

    text = str(cell).replace('\r', ' ').replace('\n', ' ')
    # Eliminar "Escala:" (con may/min y espacios) y la leyenda completa
    text = re.sub(r'(?i)\bEscala\s*:\s*', ' ', text)
    text = text.replace(scale_legend, ' ')
    text = re.sub(r'\s+', ' ', text).strip()

    # Separar por ';' los bloques de pregunta
    bloques = [b.strip() for b in text.split(';') if b.strip()]
    ans = {Q1: None, Q2: None, Q3: None, Q4: None}

    for b in bloques:
        # Pregunta abierta (Q4): viene con ":" seguido del texto libre
        if Q4 in b:
            parts = [p.strip() for p in b.split(':', 1)]
            if len(parts) == 2:
                ans[Q4] = parts[1] or None
            else:
                rest = b.replace(Q4, '').strip(' :')
                ans[Q4] = rest or None
            continue

        # Preguntas Likert: "<PREGUNTA> <RESPUESTA>"
        for q in (Q1, Q2, Q3):
            if q in b:
                resp = b.replace(q, '').strip(' :')
                # Limpieza extra por si quedara "Escala:" colgando
                resp = re.sub(r'(?i)\bEscala\s*:\s*', ' ', resp).strip()
                ans[q] = resp or None
                break
    return ans

# Aplicar limpieza/extracción
extracted = df[col_qa].apply(limpiar_y_extraer)
extracted_df = pd.DataFrame(list(extracted))

# Renombrar columnas
renamed = {
    Q1: 'Q1 - Resolver a través de Verona fue fácil',
    Q2: 'Q2 - Tiempo de atención adecuado',
    Q3: 'Q3 - El equipo del CSA entendió y acompañó',
    Q4: 'Q4 - ¿Qué podríamos mejorar?'
}
extracted_df = extracted_df.rename(columns=renamed)

# Unir con columnas originales (y remover Questions/Answers)
result_df = pd.concat([df.drop(columns=[col_qa]), extracted_df], axis=1)

# ============================================================
# 4) Mapeo de texto a números (Likert 1..5) + Feedback (num)
# ============================================================
likert_map = {
    'Totalmente en desacuerdo': 1,
    'En desacuerdo': 2,
    'Ni de acuerdo ni en desacuerdo': 3,
    'De acuerdo': 4,
    'Totalmente de acuerdo': 5,
}

for col in [
    'Q1 - Resolver a través de Verona fue fácil',
    'Q2 - Tiempo de atención adecuado',
    'Q3 - El equipo del CSA entendió y acompañó'
]:
    result_df[col] = result_df[col].astype(str).str.strip()
    result_df[col + ' (num)'] = result_df[col].map(likert_map)

# Extraer "Feedback (num)" del texto "X out of 5"
result_df['Feedback (num)'] = (
    result_df['Feedback'].astype(str)
    .str.extract(r'(\d+)')
    .astype(float)
)

# ============================================================
# 5) Calcular el Valor de Satisfacción
#    (Promedio simple de Feedback (num) y Q1..Q3 (num))
# ============================================================
num_cols = [
    'Feedback (num)',
    'Q1 - Resolver a través de Verona fue fácil (num)',
    'Q2 - Tiempo de atención adecuado (num)',
    'Q3 - El equipo del CSA entendió y acompañó (num)'
]
result_df['Satisfacción (1-5)'] = result_df[num_cols].mean(axis=1).round(2)

# ============================================================
# 6) Guardar como CSV separado por COMAS (Excel-friendly)
# ============================================================
output_path = 'jira-survey-feedback-table_QA_expandido_comas.csv'
result_df.to_csv(output_path, index=False, encoding='utf-8-sig')

# (Opcional) Ver head
result_df.head()