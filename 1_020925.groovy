https://www.metrocuadrado.com/inmueble/venta-apartamento-bogota-galerias-2-habitaciones-2-banos-1-garajes/15738-M6139674?src_url=%2Fapartamento%2Fventa%2Fusado%2Fbogota%2Fmodelo-norte%2F2-banos-330-millones%2F%3Fsearch%3Dform


import pandas as pd
import io
from google.colab import files

def procesar_jerarquia():
    # 1. Subir el archivo
    print("Por favor, sube tu archivo Excel:")
    uploaded = files.upload()
    
    # Obtener el nombre del archivo subido
    file_name = list(uploaded.keys())[0]
    df = pd.read_excel(io.BytesIO(uploaded[file_name]))
    
    # --- Configuración de nombres de columnas ---
    # Ajusta estos nombres si tu Excel tiene cabeceras distintas
    col_padre = 'padre'
    col_hijo = 'hijo'
    col_id = 'id'
    col_fecha = 'fecha'
    
    # 2. Limpieza inicial: Asegurar que la fecha sea tipo datetime
    df[col_fecha] = pd.to_datetime(df[col_fecha])
    
    # 3. Lógica de Hijo Único: Si el padre es nulo, el padre es el hijo
    df[col_padre] = df[col_padre].fillna(df[col_hijo])
    
    # 4. Calcular la fecha más actualizada (máxima) por cada Padre
    # Esto crea un mapa de: Padre -> Fecha más reciente de sus hijos
    fechas_maximas = df.groupby(col_padre)[col_fecha].max().reset_index()
    fechas_maximas.columns = [col_padre, 'fecha_actualizada']
    
    # 5. Unir la fecha actualizada al dataframe original
    df_final = df.merge(fechas_maximas, on=col_padre, how='left')
    
    # Reemplazamos la fecha original por la actualizada si queremos que todos 
    # los miembros del grupo compartan la fecha del más reciente
    df_final[col_fecha] = df_final['fecha_actualizada']
    df_final = df_final.drop(columns=['fecha_actualizada'])
    
    # 6. Descargar el resultado
    output_name = "procesado_" + file_name
    df_final.to_excel(output_name, index=False)
    print(f"\nProceso completado. Descargando: {output_name}")
    files.download(output_name)

# Ejecutar la función
procesar_jerarquia()
