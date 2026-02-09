# ==========================================================
# Encuesta Jira -> Limpieza, expansión y cálculo de satisfacción
# (Celda única para Google Colab)
# ==========================================================

import re
import pandas as pd
from google.colab import files

# ---------------------------
# 1) Subir archivo original
# ---------------------------
print("Selecciona el archivo CSV original exportado de Jira (separado por ';').")
uploaded = files.upload()  # Abre el cuadro de diálogo para subir el archivo

# Tomamos el primer archivo subido
assert len(uploaded) > 0, "No subiste ningún archivo."
input_path = list(uploaded.keys())[0]
print(f"Archivo recibido: {input_path}")

# ---------------------------
# 2) Constantes y utilidades
# ---------------------------
LIKERT_MAP = {
    'Totalmente en desacuerdo': 1,
    'En desacuerdo': 2,
    'Ni de acuerdo ni en desacuerdo': 3,
    'De acuerdo': 4,
    'Totalmente de acuerdo': 5,
}

Q1 = 'Resolver este requerimiento a través de Verona fue fácil.'
Q2 = 'El tiempo de atención de tu requerimiento fue adecuado.'
Q3 = 'El equipo del CSA entendió tu necesidad y te acompañó adecuadamente durante el proceso.'
Q4 = '¿Qué podríamos mejorar para que tu experiencia en próximos requerimientos sea mejor?'

SCALE_LEGEND = '1️⃣ Totalmente en desacuerdo → 5️⃣ Totalmente de acuerdo:'

def limpiar_y_extraer(cell: str):
    """Limpia 'Escala' y la leyenda, separa bloques por ';' y devuelve dict con Q1..Q4."""
    if pd.isna(cell):
        return {Q1: None, Q2: None, Q3: None, Q4: None}

    text = str(cell).replace('\r', ' ').replace('\n', ' ')
    # Eliminar "Escala:" (case-insensitive) y la leyenda
    text = re.sub(r'(?i)\bEscala\s*:\s*', ' ', text)
    text = text.replace(SCALE_LEGEND, ' ')
    text = re.sub(r'\s+', ' ', text).strip()

    bloques = [b.strip() for b in text.split(';') if b.strip()]
    ans = {Q1: None, Q2: None, Q3: None, Q4: None}

    for b in bloques:
        # Pregunta abierta (Q4): "<PREGUNTA>: texto libre"
        if Q4 in b:
            parts = [p.strip() for p in b.split(':', 1)]
            if len(parts) == 2:
                ans[Q4] = parts[1] or None
            else:
                rest = b.replace(Q4, '').strip(' :')
                ans[Q4] = rest or None
            continue

        # Likert Q1..Q3: "<PREGUNTA> <RESPUESTA>"
        for q in (Q1, Q2, Q3):
            if q in b:
                resp = b.replace(q, '').strip(' :')
                resp = re.sub(r'(?i)\bEscala\s*:\s*', ' ', resp).strip()
                ans[q] = resp or None
                break
    return ans

# ---------------------------
# 3) Cargar y procesar dataset
# ---------------------------
df = pd.read_csv(input_path, sep=';', dtype=str, encoding='utf-8')
df.columns = [c.strip() for c in df.columns]

# Algunas exportaciones incluyen una fila residual 'sep=': la eliminamos
if 'Ticket-id' in df.columns:
    df = df[df['Ticket-id'].astype(str).str.strip() != 'sep=']

# Validación de columna
assert 'Questions/Answers' in df.columns, "No se encontró la columna 'Questions/Answers' en el CSV."

# Extraer Q1..Q4 desde el texto en bloque
extracted = df['Questions/Answers'].apply(limpiar_y_extraer)
extracted_df = pd.DataFrame(list(extracted)).rename(columns={
    Q1: 'Q1 - Resolver a través de Verona fue fácil',
    Q2: 'Q2 - Tiempo de atención adecuado',
    Q3: 'Q3 - El equipo del CSA entendió y acompañó',
    Q4: 'Q4 - ¿Qué podríamos mejorar?'
})

# Unir con las columnas originales (quitando Questions/Answers)
result_df = pd.concat([df.drop(columns=['Questions/Answers']), extracted_df], axis=1)

# ---------------------------
# 4) Mapeo a números + Feedback (num)
# ---------------------------
for col in [
    'Q1 - Resolver a través de Verona fue fácil',
    'Q2 - Tiempo de atención adecuado',
    'Q3 - El equipo del CSA entendió y acompañó'
]:
    result_df[col] = result_df[col].astype(str).str.strip()
    result_df[col + ' (num)'] = result_df[col].map(LIKERT_MAP)

# Extraer "Feedback (num)" desde "X out of 5"
result_df['Feedback (num)'] = (
    result_df['Feedback'].astype(str)
    .str.extract(r'(\d+)')
    .astype(float)
)

# ---------------------------
# 5) Calcular Satisfacción (1-5)
#    Promedio simple de: Feedback (num), Q1(num), Q2(num), Q3(num)
# ---------------------------
num_cols = [
    'Feedback (num)',
    'Q1 - Resolver a través de Verona fue fácil (num)',
    'Q2 - Tiempo de atención adecuado (num)',
    'Q3 - El equipo del CSA entendió y acompañó (num)'
]
result_df['Satisfacción (1-5)'] = result_df[num_cols].mean(axis=1).round(2)

# --- (OPCIONAL) Ponderación alternativa ---
# Descomenta si quieres ponderar (ejemplo: 20% Feedback, 30% Q1, 25% Q2, 25% Q3)
# result_df['Satisfacción (1-5)'] = (
#     result_df['Feedback (num)'] * 0.20 +
#     result_df['Q1 - Resolver a través de Verona fue fácil (num)'] * 0.30 +
#     result_df['Q2 - Tiempo de atención adecuado (num)'] * 0.25 +
#     result_df['Q3 - El equipo del CSA entendió y acompañó (num)'] * 0.25
# ).round(2)

# ---------------------------
# 6) Orden sugerido de columnas
# ---------------------------
ordered_cols = [
    'Ticket-id','Ticket summary','Survey Name','Feedback','Feedback (num)',
    'Assignee','Answered By','Answered On','Comment',
    'Q1 - Resolver a través de Verona fue fácil','Q1 - Resolver a través de Verona fue fácil (num)',
    'Q2 - Tiempo de atención adecuado','Q2 - Tiempo de atención adecuado (num)',
    'Q3 - El equipo del CSA entendió y acompañó','Q3 - El equipo del CSA entendió y acompañó (num)',
    'Q4 - ¿Qué podríamos mejorar?','Satisfacción (1-5)'
]
ordered_cols = [c for c in ordered_cols if c in result_df.columns]
result_df = result_df[ordered_cols]

print("Procesamiento completado. Vista previa:")
display(result_df.head())

# ---------------------------
# 7) Guardar y descargar CSV con COMAS (Excel-friendly)
# ---------------------------
output_path = 'jira-survey-feedback-table_QA_expandido_comas.csv'
result_df.to_csv(output_path, index=False, encoding='utf-8-sig')
print(f"Archivo generado: {output_path}")

# Descargar al equipo
files.download(output_path)