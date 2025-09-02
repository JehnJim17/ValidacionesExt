// Configuración inicial
def issueKey = "CSA-2590"
def jsonFields = get("/rest/api/2/issue/${issueKey}").asObject(Map).body
logger.info "Issue: ${JsonOutput.toJson(jsonFields)}"
if (issue.customfield_13044?.value == 'Curso de supervisor')
{
def baseUrl2 = "https://api.atlassian.com/jsm/assets/workspace/${workspaceId}/v1/object/create"
def baseUrl3 = "https://jira-ath.atlassian.net/rest/api/3/issue/${issueKey}"

// === OBTENER DATOS DEL OBJETO DE EVALUACION SELECCIONADA NOMBRE ===
def issue = get("${baseUrl3}")
    .header("Content-Type", "application/json")
    .basicAuth(usuario,token)
    .asObject(Map)
    .body

logger.info "IssueV3: ${JsonOutput.toJson(issue)}"
def usuarioRadicadorEmail = issue?.fields?.creator?.emailAddress
logger.info "UsaurioRadicadorEmail: ${usuarioRadicadorEmail}"
def usuarioRadicadorName = issue?.fields?.creator?.displayName
logger.info "UsaurioRadicadorName: ${usuarioRadicadorName}"
def usuarioRadicadorNAccountId = issue?.fields?.creator?.accountId
logger.info "UsaurioRadicadorAccountId: ${usuarioRadicadorNAccountId}"
String Entidad = jsonFields['fields']["customfield_11319"]['objectId']
Entidad = Entidad.replace("[", "AB-").replace("]", "")
logger.info "Entidad: ${Entidad}"
def fecha = new Date().format("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", TimeZone.getTimeZone("UTC"))

// Definimos el mapa de respuestas válidas
def respuestasValidas = [
    "13526": "Falso",
    "13527": "Gerente, director o vicepresidente",
    "13528": "Verdadero",
    "13529": "En el Memorando de presentación de solicitud de compra o contratación para aprobación del comité",
    "13530": "Falso",
    "13531": "Falso",
    "13532": "Ninguna de las anteriores",
    "13533": "Verdadero",
    "13534": "Verdadero",
    "13535": "Dirección Cadena de Abastecimiento",
    "13536": "Verdadero",
    "13537": "Entregar toda la información a su cargo, actualizada a la fecha de entrega al nuevo Supervisor, realizando el empalme y dejando informe de entrega por escrito",
    "13538": "Administrativo, técnico, legal y financiero",
    "13539": "Falso",
    "13540": "Verdadero",
    "13541": "Todas las anteriores",
    "13542": "Ninguna de las anteriores",
    "13543": "Informar oportunamente y por escrito a la Dirección de Control y Cumplimiento del Banco y a la de Abastecimiento y Logística sobre hechos que puedan constituir corrupción, delitos o riesgos para la correcta ejecución contractual.",
    "13544": "1 y 2 son correctos",
    "13545": "Verdadero",
    "13546": "Todas las anteriores",
    "13547": "Verdadero",
    "13548": "Falso",
    "13549": "Verdadero",
    "13550": "Prohibición del Supervisor del Contrato"
]

// Función traevalor
def traevalor(def jsonFields, def cf, def respuestasValidas) {
    def valor = jsonFields['fields'][cf]
    if (valor instanceof Map && valor.containsKey("value")) {
        def texto = valor["value"]?.toString()?.trim()
        def cfNumber = cf.replace("customfield_", "") // Sacamos solo el número
        
        // Comprobamos si el valor es válido
        return (respuestasValidas.containsKey(cfNumber) && respuestasValidas[cfNumber] == texto) ? 1 : 0
    }
    return 0
}

// Lista de campos personalizados (13526 a 13550)
def customFields = (13526..13550).collect { "customfield_${it}" }

// Obtener los valores y calcular el promedio en un solo paso
def preguntas = customFields.collect { cf -> traevalor(jsonFields, cf, respuestasValidas) }

// Para saber las respuestas incorrectas
def respuestasIncorrectas = []
customFields.eachWithIndex { cf, index ->
    def valor = jsonFields['fields'][cf]
    def texto = valor?.value?.toString()?.trim()
    def cfNumber = cf.replace("customfield_", "")
    
    if (!respuestasValidas.containsKey(cfNumber) || respuestasValidas[cfNumber] != texto) {
        respuestasIncorrectas << [pregunta: cfNumber, respuesta: texto]
    }
}

// Calcular promedio y porcentaje
def promedio = preguntas.sum() / preguntas.size()
def porcentaje = Math.round(promedio * 100)

// Log de resultados
logger.info "Promedio: ${promedio}"
logger.info "Porcentaje: ${porcentaje}%"
logger.info "Respuestas incorrectas: ${respuestasIncorrectas.size()}"
logger.info "Respuestas incorrectas detalles: ${respuestasIncorrectas}"

def eventIssue = Issues.getByKey(issueKey as String)

// Lógica condicional basada en el porcentaje
if (porcentaje >= 80) {
        // Crear el Objeto
    def Suepervisorfinal = post("${baseUrl2}")
        .header("Content-Type", "application/json")
        .basicAuth(usuario, token)
        .body([
            attributes: [
                [
                    //Nombre
                    objectTypeAttributeId: "1181",
                    objectAttributeValues: [[ value: "${usuarioRadicadorName}" ]]
                ],
                [
                    //Correo
                    objectTypeAttributeId: "1184",
                    objectAttributeValues: [[ value: "${usuarioRadicadorEmail}" ]]
                ],
                [
                    //Correo2
                    objectTypeAttributeId: "1689",
                    objectAttributeValues: [[ value: "${usuarioRadicadorNAccountId}" ]]
                ],
                [
                    //Nombre
                    objectTypeAttributeId: "1185",
                    objectAttributeValues: [[ value: "${usuarioRadicadorName}" ]]
                ],
                [
                    //entidad
                    objectTypeAttributeId: "1186",
                    objectAttributeValues: [[ value: "${Entidad}" ]]
                ],
                [
                    //flag
                    objectTypeAttributeId: "1187",
                    objectAttributeValues: [[ value: "true" ]]
                ],
                [
                    //Score
                    objectTypeAttributeId: "1188",
                    objectAttributeValues: [[ value: "${porcentaje}" ]]
                ],
                [
                    //Fecha
                    objectTypeAttributeId: "1189",
                    objectAttributeValues: [[ value: "${fecha}" ]]
                ]
            ],
            objectTypeId: "116"
        ])
        .asObject(Map)

    logger.info "res: ${JsonOutput.toJson(Suepervisorfinal)}"

    // Comentario de éxito
    def comment = "Su calificación es ${porcentaje}. Usted ya fue creado como Usuario Supervisor."
    eventIssue.addComment(comment)
} else {
    // Comentario de fallo
    def comment = "Su calificación es ${porcentaje} y está por debajo del valor de aceptación. Por favor, dirijete de nuevo a hacer el curso y responder las preguntas. Solo se puede ser supervisor si supera el 80%."
    eventIssue.addComment(comment)
}

 
 //Transicionar a "Cambio Aprobado"
post("/rest/api/3/issue/${issueKey}/transitions")
    .header("Content-Type", "application/json")
    .body([
        transition: [ id: "91" ]
    ])
    .asString()
}