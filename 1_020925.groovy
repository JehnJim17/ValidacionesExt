<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8" />
  <title>Mis Consultas | JSM</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />

  <style>
    :root{
      --azul:#002b49;
      --gris-bg:#f7f9fc;
      --card-radius:20px;
    }
    body{
      margin:0; padding:0;
      font-family:'Segoe UI', sans-serif;
      background:var(--gris-bg);
      color:#222;
    }
    .u-hidden{display:none !important;}
    .u-visually-hidden{display:none;}
    .u-max-width-200{max-width:200px;}

    #fit-wrapper { width: 100%; }
    #app-root { width: 100%; }

    /* ---------- BANNER ---------- */
    .banner{
      width:100%;
      background:var(--azul);
      color:#fff;
      display:flex;
      align-items:center;
      justify-content:center;
      padding:0.5rem 2rem;
      box-shadow:0 2px 8px rgba(0,43,73,0.07);
      box-sizing:border-box;
      min-height:64px;
      position:relative;
      z-index:5;
    }
    .banner-title{
      font-size:1.35rem;
      font-weight:600;
      letter-spacing:0.02em;
      text-align:center;
    }
    .banner-back-circle{
      position:absolute;
      left:12px;
      top:50%;
      transform:translateY(-50%);
      width:36px;
      height:36px;
      border-radius:50%;
      border:1px solid rgba(255,255,255,.6);
      background:transparent;
      color:#fff;
      display:flex;
      align-items:center;
      justify-content:center;
      font-size:18px;
      cursor:pointer;
    }
    .banner-back-circle:hover{background:rgba(255,255,255,.12);}

    /* ---------- HOME ---------- */
    #front-home{
      position:relative;
      min-height:100vh;
      background:url("https://jira-ath.atlassian.net/wiki/download/attachments/288161801/Mis%20Consultas%20-%20Verona.png?api=v2") center/cover no-repeat;
      display:flex;
      align-items:center;
      justify-content:center;
      overflow:hidden;
    }
    .home-inner{
      width:90%;
      max-width:1200px;
      display:grid;
      grid-template-columns:1fr 1fr;
      column-gap:5rem;
      align-items:center;
    }
    .home-left,.home-right{
      display:flex;
      flex-direction:column;
      align-items:center;
      text-align:center;
    }
    .hero-row{
      display:flex;
      align-items:center;
      justify-content:center;
      gap:2rem;
      flex-wrap:nowrap;
    }
    .home-img{
      max-width:150px;
      height:auto;
      margin-bottom:1rem;
    }
    .home-title{
      color:#fff;
      font-size:4rem;
      line-height:.9;
      font-weight:700;
      text-shadow:0 2px 6px rgba(0,0,0,.4);
    }
    .home-card{
      background:#fff;
      border-radius:var(--card-radius);
      padding:2.5rem 3rem;
      box-shadow:0 10px 25px rgba(0,0,0,.18);
      width:100%;
      max-width:320px;
      gap:1.8rem;
      display:flex;
      flex-direction:column;
    }
    .btn-main{
      padding:0.9rem 1.2rem;
      background:var(--azul);
      color:#fff;
      border:none;
      border-radius:8px;
      cursor:pointer;
      font-size:1rem;
      letter-spacing:0.08em;
      transition:transform .1s ease, box-shadow .1s ease;
      width:100%;
    }
    .btn-main:hover{transform:translateY(-2px); box-shadow:0 6px 16px rgba(0,0,0,.18);}
    .btn-main.secondary{background:#fff; color:var(--azul); border:1px solid var(--azul);}
    .btn-secondary {
        padding: 0.5rem 1rem;
        background: #eef1f5;
        color: var(--azul);
        border: 1px solid #d8dfe6;
        border-radius: 4px;
        cursor: pointer;
    }

    @media(max-width:768px){
      .home-inner{grid-template-columns:1fr; row-gap:3rem;}
      .hero-row{flex-direction:column; text-align:center;}
    }

    /* ---------- FRONT EMAIL / WAIT ---------- */
    .centered{
      display:flex;
      flex-direction:column;
      align-items:center;
      justify-content:center;
      min-height:calc(100vh - 64px);
      padding:1.5rem;
      text-align:center;
    }
    .input-email{
      width:260px;
      padding:0.6rem 0.8rem;
      font-size:1rem;
      border-radius:6px;
      border:1px solid #ccc;
      margin-bottom:1rem;
    }
    .error{color:#d0021b; margin-top:0.5rem;}
    .spinner{
      width:48px; height:48px;
      border:5px solid #e0e0e0; border-top:5px solid var(--azul);
      border-radius:50%; animation:spin 1s linear infinite; margin:1rem auto;
    }
    @keyframes spin{to{transform:rotate(360deg);} }

    /* ---------- FRONT FILTROS ---------- */
    .page{display:flex; min-height:calc(100vh - 64px);}
    .sidebar{
      width:260px; background:#fff; padding:1.2rem 1rem;
      border-right:1px solid #ddd; box-sizing:border-box;
      transition: display .2s;
    }
    .sidebar h3{margin:0 0 1rem;}
    .filter-group{margin-bottom:1rem;}
    .filter-group label{
      display:block;
      margin-bottom:0.3rem;
      font-weight:600;
      font-size:0.9rem;
    }
    select,button{
      width:100%;
      padding:0.5rem;
      font-size:1rem;
      border-radius:4px;
      border:1px solid #ccc;
      cursor:pointer;
    }
    select:disabled {
        background-color: #eee;
        cursor: not-allowed;
    }
    .filter-actions{display:flex; flex-direction:column; gap:0.5rem; margin-top:1rem;}
    .content{flex:1; padding:1rem 1.5rem; box-sizing:border-box; position:relative;}
    
    .page.no-sidebar .sidebar {
        display: none;
    }

    .table-actions{
      display: flex;
      justify-content: flex-end;
      align-items: center;
      margin-bottom: 0.5rem;
      gap: 1.5rem;
      flex-wrap: wrap;
    }
    .action-group {
      display: flex;
      align-items: center;
      gap: 0.75rem;
    }
    .action-group label {
      font-size: 0.9rem;
      margin-bottom: 0;
      white-space: nowrap;
    }
    .action-group select {
      width: auto;
    }
    #results-count {
      font-size: 0.9rem;
      color: #555;
      margin-right: auto;
    }
    .btn-icon {
      background: transparent;
      border: 1px solid #ccc;
      border-radius: 4px;
      width: 38px;
      height: 38px;
      padding: 0;
      display: flex;
      align-items: center;
      justify-content: center;
      cursor: pointer;
      color: var(--azul);
      transition: background-color 0.2s;
    }
    .btn-icon:hover {
      background-color: #f0f0f0;
    }
    .btn-icon svg {
      width: 20px;
      height: 20px;
    }

    .results-container{
      overflow-x:auto;
      background:#fff;
      padding:1rem;
      border-radius:4px;
      box-shadow:0 2px 4px rgba(0,0,0,0.05);
    }
    table{
      width:100%; 
      border-collapse:collapse; 
      font-size:0.92rem;
      table-layout: fixed;
    }
    th,td{
      padding:0.5rem; 
      border-left: none;
      border-right: none;
      border-top: 1px solid #ddd;
      border-bottom: 1px solid #ddd; 
      text-align: left;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }
    th{
      background:#f0f0f0;
      position: relative;
      border-top: none;
    }
    .resize-handle {
        position: absolute;
        top: 0;
        right: -4px;
        width: 8px;
        height: 100%;
        cursor: col-resize;
        z-index: 1;
    }

    #loading-msg{margin:0.5rem 0;}
    
    .pagination-controls {
      display: flex;
      justify-content: center;
      align-items: center;
      gap: 1rem;
      margin-top: 1rem;
      padding-bottom: 1rem;
    }
    .pagination-controls .page-info {
      font-size: 0.9rem;
      color: #555;
    }
    .pagination-controls button {
      width: 40px;
      height: 40px;
      background: #fff;
      color: var(--azul);
      border: 1px solid #ccc;
      cursor: pointer;
      transition: background-color .2s;
      font-weight: bold;
      padding: 0;
    }
    .pagination-controls button:hover:not(:disabled) {
      background-color: #f0f0f0;
    }
    .pagination-controls button:disabled {
      cursor: not-allowed;
      opacity: 0.5;
    }

    .th-content {
        display: flex;
        align-items: center;
        justify-content: space-between;
        gap: 4px;
    }
    .filter-icon {
        cursor: pointer;
        color: #888;
        flex-shrink: 0;
    }
    .filter-icon:hover {
        color: #333;
    }
    .filter-icon.active {
        color: var(--azul);
    }
    
    .filter-dropdown {
        background-color: white;
        border: 1px solid #ccc;
        box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        z-index: 1000;
        min-width: 220px;
        max-height: 300px;
        display: flex;
        flex-direction: column;
    }
    .filter-dropdown-list {
        list-style: none;
        padding: 10px;
        margin: 0;
        overflow-y: auto;
        flex-grow: 1;
    }
    .filter-dropdown-list li {
        padding: 4px 0;
    }
    .filter-dropdown-actions {
        display: flex;
        justify-content: space-between;
        border-top: 1px solid #eee;
        padding: 8px 10px;
        background-color: white;
        flex-shrink: 0;
    }
    .filter-dropdown-actions button {
        width: auto;
        padding: 4px 8px;
        font-size: 0.85rem;
    }
    .filter-search-input {
        width: 100%;
        padding: 6px 8px;
        margin: 8px 10px 0;
        width: calc(100% - 20px);
        border-radius: 4px;
        border: 1px solid #ccc;
        box-sizing: border-box;
    }
    
    #debug-container {
      margin-top: 1.5rem;
      padding: 1rem;
      background-color: #2d2d2d;
      color: #f8f8f2;
      border-radius: 4px;
      font-family: monospace;
      text-align: left;
      max-height: 400px;
      overflow-y: auto;
      border: 1px solid #444;
    }
    #debug-container h4 {
      margin-top: 0;
      margin-bottom: 0.5rem;
      color: #ccc;
      font-weight: 600;
      border-bottom: 1px solid #555;
      padding-bottom: 0.5rem;
    }
    #debug-output {
      white-space: pre-wrap;
      word-break: break-all;
    }
    
    /* === ESTILOS PARA LA VISTA DE DETALLE === */
    tr.contract-row {
        cursor: pointer;
        transition: background-color 0.2s;
    }
    tr.contract-row:hover {
        background-color: #e9f2ff;
    }
    tr.is-expanded {
        background-color: #deebff !important;
    }
    tr.is-expanded td {
        border-bottom-color: transparent;
    }
    tr.detail-row > td {
        padding: 0 !important;
        border: 0 !important;
    }
    .contract-card-wrapper {
        padding: 1.5rem;
        background: #f7f9fc;
        border-top: 2px solid #0052cc;
    }
    .contract-card-wrapper .card-header {
        background-color: transparent;
        color: var(--azul);
        display: flex;
        align-items: center;
        padding: 0 0 12px 0;
        border-bottom: 1px solid #dfe1e6;
        margin-bottom: 12px;
    }
    .contract-card-wrapper .card-header img{
      margin-right: 12px;
      border-radius: 4px;
      background-color: white;
    }
    .contract-card-wrapper .card-header h2{
        margin: 0;
        font-size: 1.2em;
    }
    .contract-card-wrapper .card-body {
        background-color: transparent;
        padding: 0;
    }

    .attribute-list { 
      list-style-type: none; 
      padding: 0; 
      margin: 0;
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 0 24px;
    }
    .attribute-list li { 
      padding: 9px 0; 
      border-bottom: 1px solid #f0f0f0; 
      align-items: start; 
    }
    .attribute-list .label { 
      font-weight: bold; 
      color: #5e6c84; 
      font-size: 0.85rem;
      display: block;
      margin-bottom: 4px;
    }
    .attribute-list .value { color: #172b4d; }
    
    .attribute-tags-container {
      display: flex;
      flex-wrap: wrap;
      gap: 6px;
    }
    .attribute-tag {
      display: inline-block;
      background-color: #e9ebee;
      color: #444;
      padding: 4px 10px;
      border-radius: 12px;
      font-size: 0.85rem;
      border: 1px solid #d8dfe6;
    }
    .card-footnote {
      margin-top: 1.5rem;
      font-size: 0.8rem;
      color: #666;
      text-align: center;
      border-top: 1px solid #dfe1e6;
      padding-top: 1rem;
    }
    /* === ESTILOS NUEVOS PARA DETALLES DE SOLICITUDES === */
    .attribute-list .full-width-attribute {
        grid-column: 1 / -1;
    }
    .related-issue-tag {
        display: inline-flex;
        align-items: center;
        gap: 6px;
        background-color: #dfe1e6;
        color: #172b4d;
        padding: 4px 10px;
        border-radius: 16px;
        font-size: 0.85rem;
        border: 1px solid #c1c7d0;
        text-decoration: none;
    }
    .related-issue-tag:hover {
        background-color: #c1c7d0;
        color: #091e42;
    }
    .related-issue-tag .issue-type-icon {
        width: 16px;
        height: 16px;
    }
    .related-issue-tag .issue-link-type {
        color: #5e6c84;
    }
    .related-issue-tag .issue-key {
        font-weight: 600;
    }
    .days-remaining-ok {
        color: #006400; /* Verde oscuro */
        font-weight: bold;
    }
    .days-remaining-due {
        color: #ff8c00; /* Naranja oscuro */
        font-weight: bold;
    }
    .days-remaining-overdue {
        color: #d0021b; /* Rojo */
        font-weight: bold;
    }
    .related-issue-tag .issue-type-name {
    color: #444;
    white-space: nowrap;
    }
    .related-issue-tag .issue-summary {
        color: #5e6c84;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        max-width: 250px; /* Ajusta este valor si quieres más o menos texto */
    }
    .card-header {
    justify-content: space-between; /* Alinear contenido del header */
    }
    .header-content {
        display: flex;
        align-items: center;
    }
    .header-content img {
        margin-right: 12px;
    }
    .view-issue-link {
        background-color: #0052cc;
        color: #fff !important;
        padding: 6px 12px;
        border-radius: 4px;
        font-size: 0.85rem;
        font-weight: 600;
        text-decoration: none;
        white-space: nowrap;
        transition: background-color 0.2s;
    }
    .view-issue-link:hover {
        background-color: #0065ff;
    }
  </style>

</head>
<body>

  <div id="fit-wrapper">
    <div id="app-root">

      <header id="banner" class="banner u-hidden">
        <button id="btn-back-banner" class="banner-back-circle u-hidden" aria-label="Volver">←</button>
        <h1 id="banner-title" class="banner-title"></h1>
      </header>

      <section id="front-home">
        <div class="home-inner">
          <div class="home-left">
            <div class="hero-row">
              <img class="home-img" src="https://jira-ath.atlassian.net/wiki/download/attachments/287670279/Logo%20(1)%20(1).png?api=v2" alt="Logo de la aplicación">
              <div class="home-copy">
                <h2 class="home-title">Mis<br>Consultas</h2>
              </div>
            </div>
          </div>
          <div class="home-right">
            <div class="home-card">
              <button id="btn-go-solicitudes" class="btn-main">SOLICITUDES</button>
              <button id="btn-go-contratos" class="btn-main secondary">CONTRATOS</button>
            </div>
          </div>
        </div>
      </section>

      <div id="front-email" class="centered u-hidden">
        <h2>Validar acceso</h2>
        <p>Ingrese su correo institucional:</p>
        <input id="input-email" type="email" class="input-email" placeholder="nombre@empresa.com" required />
        <button id="btn-enviar-email" class="btn-main u-max-width-200">Validar</button>
        <p id="msg-email-error" class="error u-visually-hidden"></p>
      </div>

      <div id="front-espera" class="centered u-hidden">
        <h2>Validando usuario...</h2>
        <div class="spinner"></div>
        <p>Revisa tu correo y aprueba la solicitud.<br>Esta ventana se actualizará automáticamente.</p>
        <p id="msg-check-error" class="error u-visually-hidden"></p>
        <button id="btn-rejection-home" class="btn-main u-max-width-200 u-visually-hidden" style="margin-top: 1rem;">Volver al Inicio</button>
      </div>
      
      <div id="front-cargando-contratos" class="centered u-hidden">
        <h2>Obteniendo los contratos...</h2>
        <div class="spinner"></div>
        <p id="msg-contracts-error" class="error u-visually-hidden"></p>
      </div>

      <div id="front-filtros" class="page u-hidden">
        <aside class="sidebar">
          <h3>Filtros</h3>
          <div class="filter-group">
            <label for="project">Proyecto</label>
            <select id="project" disabled>
              <option value="CSA">CSA</option>
              <option value="DEV">DEV</option>
              <option value="INF">INF</option>
            </select>
          </div>
          <div class="filter-group">
            <label for="entidad">Entidad</label>
            <select id="entidad">
              <option value="">Cargando...</option>
            </select>
          </div>
          <div class="filter-group">
            <label for="estructura">Dirección</label>
            <select id="estructura" disabled>
              <option value="">Seleccione una entidad primero</option>
            </select>
          </div>
          <div class="filter-group">
            <label for="consulta">Consultar</label>
            <select id="consulta">
              <option value="Contratos">Contratos</option>
              <option value="Casos">Casos</option>
            </select>
          </div>
          <div class="filter-actions">
            <button id="btn-consultar">Consultar</button>
            <button id="btn-reset">Reset filtros</button>
            <button id="btn-volver" class="u-visually-hidden">Volver</button>
          </div>
        </aside>

        <main class="content">
          <p id="loading-msg" class="u-visually-hidden">Cargando...</p>
          <p id="error-msg" class="error u-visually-hidden"></p>

          <div id="results" class="u-visually-hidden">
            <div class="table-actions">
              <span id="results-count"></span>
              <button id="btn-clear-client-filter" class="btn-secondary u-hidden">Limpiar Filtros de Tabla</button>
              <div class="action-group">
                <label for="rows-per-page">Ver:</label>
                <select id="rows-per-page">
                  <option value="10">10</option>
                  <option value="25">25</option>
                  <option value="50">50</option>
                </select>
              </div>
              <div class="action-group">
                <button id="btn-export" class="btn-icon" title="Exportar a CSV">
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor"><path d="M19.35 10.04C18.67 6.59 15.64 4 12 4 9.11 4 6.6 5.64 5.35 8.04 2.34 8.36 0 10.91 0 14c0 3.31 2.69 6 6 6h13c2.76 0 5-2.24 5-5 0-2.64-2.05-4.78-4.65-4.96zM17 13l-5 5-5-5h3V9h4v4h3z"/></svg>
                </button>
              </div>
            </div>
            <div class="results-container">
              <table id="results-table">
                <thead></thead>
                <tbody></tbody>
              </table>
            </div>
            <div id="pagination-controls" class="pagination-controls"></div>
          </div>
          <p id="no-results" class="u-visually-hidden">No hay resultados.</p>
          
          <div id="debug-container" class="u-hidden">
            <h4>Respuesta del Webhook:</h4>
            <pre id="debug-output"></pre>
          </div>

        </main>
      </div>

    </div>
  </div>
<script>
document.addEventListener('DOMContentLoaded', () => {

  // =================================
  //  CONFIGURATION
  // =================================

  const CONFIG = {
      apiUrls: {
          getInitialFilters: 'https://prod-10.brazilsouth.logic.azure.com:443/workflows/60b2bac417d246198de3b9e47e93f36c/triggers/manual/paths/invoke?api-version=2016-06-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=x7FX9Rxkb2oJBUPsQxxXI1hw13dehQSavUkw2xYEZsI',
          sendEmail: 'https://prod-10.brazilsouth.logic.azure.com:443/workflows/e205382177a348269b215a6181df21a1/triggers/manual/paths/invoke?api-version=2016-06-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=xAXpqw3DTJOY7GRTzV0Pi5_kz2pBzV3AFXql0YpZR_M',
          checkApproval: 'https://prod-26.brazilsouth.logic.azure.com:443/workflows/d47015a83a3c4d41836c104968fdf549/triggers/manual/paths/invoke?api-version=2016-06-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=3VuwnNNd1Dlp9faYtXzw7DrH4tY5KKfVoRESOuFvUiI',
          contratos: 'https://prod-07.brazilsouth.logic.azure.com:443/workflows/c4683c076bc74d74a8e0fcba4c4e7f66/triggers/manual/paths/invoke?api-version=2016-06-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=hBQh-e9IDM-q0nn0f2xypMtORgFeeYjVoJG-Co1sHb8',
          /*solicitudes: 'https://prod-26.brazilsouth.logic.azure.com:443/workflows/5ee25d973efb4b52b08e42f2538bec7c/triggers/manual/paths/invoke?api-version=2016-06-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=i1ixWXdpAaN-5yanRdmRBVjz4dKZQLaX84SdO8fa-ag'*/
          solicitudes: 'https://prod-30.brazilsouth.logic.azure.com:443/workflows/867ea1c562d7458bac2f0e6a9e8c6bfb/triggers/manual/paths/invoke?api-version=2016-06-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=A6QOjuXOUzhCatM28QwAZj39r11VI6arZkaY7Kfh4KI'
      },
      attributeIds: {
          ID: '645',
          ENTIDAD: '658',
          DIRECCION_GERENCIA: '659',
          PROVEEDOR: '1107',
          CLM_HIJO: '1108',
          CLM_PADRE: '1113',
          TITULO: '1125',
          OBJETO: '1112',
          ALCANCE: '764',
          ESPECIE: '769',
          GENERO: '775',
          POLIZAS: '1110',
          CLAUSULAS: '1111',
          MATERIALES_UNSPSC: '1117',
          FECHA_INICIO: '1396',
          FECHA_FIN: '1397',
          ADMINISTRADOR: '1690',
          ADMINISTRADOR_SUPLENTE: '1691',
          TACTICO_1: '1473',
          TACTICO_2: '1599',
          TACTICO_3: '1600',
          ES_CONTRATO_MARCO: '1598',
          MONTO_COP: '1609',
          ES_CONTRATO_CORPORATIVO: '1639',
          VICEPRESIDENCIA: '280',
          ENTIDAD_EN_DIRECCION: '657',
          CONTRATO_EN_RENOVACION_CSA: '11930'
      },
      solicitudes: {
          mainFields: [
            { header: 'Key', jsonPath: 'key', isFilterable: true },
            { header: 'Summary', jsonPath: 'fields.summary', isFilterable: false },
            { header: 'Status', jsonPath: 'fields.status.name', isFilterable: true },
            { header: 'Asignado', jsonPath: 'fields.assignee.emailAddress', isFilterable: true },
            { header: 'Reportado por', jsonPath: 'fields.creator.displayName', isFilterable: true },
            { header: 'Fecha Creación', jsonPath: 'fields.created', isFilterable: true },
            { header: 'Tipo de Solicitud', jsonPath: 'fields.customfield_10010.requestType.name', isFilterable: true },
            { header: 'Categoría', jsonPath: 'fields.issuetype.name', isFilterable: true }
          ]
      }
  };

  const tableConfigs = {
    // Esta sección ya no es necesaria para solicitudes, pero la mantenemos para contratos
    contratos: [
        { header: 'ID', dataKey: 'id_attr', isFilterable: true },
        { header: 'Entidad', dataKey: 'entidad', isFilterable: true },
        { header: 'Dirección/Gerencia', dataKey: 'direccion_gerencia', isFilterable: true },
        { header: 'Proveedor', dataKey: 'proveedor', isFilterable: true },
        { header: 'CLM_Hijo', dataKey: 'clm_hijo', isFilterable: true },
        { header: 'CLM_Padre', dataKey: 'clm_padre' , isFilterable: true },
        { header: 'Título', dataKey: 'titulo' },
        { header: 'Fecha_Inicio', dataKey: 'fecha_inicio' },
        { header: 'Fecha_Fin', dataKey: 'fecha_fin' },
        { header: 'Monto en COP', dataKey: 'monto_cop' }
    ]
  };

  const state = {
    currentUserEmail: null,
    currentMode: null, 
    userProfile: null,
    rawData: null,
    filteredData: [],
    clientSideFilters: {},
    filtersLoaded: false,
    allDirecciones: [],
    polling: {
      timer: null,
      attempts: 0,
      maxAttempts: 30,
      interval: 10000,
    },
    pagination: {
      currentPage: 1,
      rowsPerPage: 10,
    }
  };

  const dom = {
    banner: {
      container: document.getElementById('banner'),
      title: document.getElementById('banner-title'),
      backBtn: document.getElementById('btn-back-banner'),
    },
    views: {
      home: document.getElementById('front-home'),
      email: document.getElementById('front-email'),
      wait: document.getElementById('front-espera'),
      filters: document.getElementById('front-filtros'),
      cargandoContratos: document.getElementById('front-cargando-contratos'),
    },
    home: {
      goSolicitudesBtn: document.getElementById('btn-go-solicitudes'),
      goContratosBtn: document.getElementById('btn-go-contratos'),
    },
    emailForm: {
      input: document.getElementById('input-email'),
      submitBtn: document.getElementById('btn-enviar-email'),
      errorMsg: document.getElementById('msg-email-error'),
    },
    wait: {
      errorMsg: document.getElementById('msg-check-error'),
      rejectionHomeBtn: document.getElementById('btn-rejection-home'),
    },
    contracts: {
        errorMsg: document.getElementById('msg-contracts-error'),
        spinner: document.querySelector('#front-cargando-contratos .spinner')
    },
    filters: {
      project: document.getElementById('project'),
      entidad: document.getElementById('entidad'),
      estructura: document.getElementById('estructura'),
      consulta: document.getElementById('consulta'),
      consultarBtn: document.getElementById('btn-consultar'),
      resetBtn: document.getElementById('btn-reset'),
      volverBtn: document.getElementById('btn-volver'),
    },
    results: {
      container: document.getElementById('results'),
      table: document.getElementById('results-table'),
      tableHeader: document.querySelector('#results-table thead'),
      tableBody: document.querySelector('#results-table tbody'),
      exportBtn: document.getElementById('btn-export'),
      loadingMsg: document.getElementById('loading-msg'),
      errorMsg: document.getElementById('error-msg'),
      noResultsMsg: document.getElementById('no-results'),
      paginationControls: document.getElementById('pagination-controls'),
      rowsPerPageSelect: document.getElementById('rows-per-page'),
      resultsCount: document.getElementById('results-count'),
      clearClientFilterBtn: document.getElementById('btn-clear-client-filter'),
    },
    debug: {
        container: document.getElementById('debug-container'),
        output: document.getElementById('debug-output')
    }
  };

  // =================================
  //  HELPERS
  // =================================

  /**
   * Obtiene de forma segura un valor anidado de un objeto usando una ruta de string.
   * @param {object} obj - El objeto del que se extraerá el valor.
   * @param {string} path - La ruta al valor (ej. 'fields.status.name').
   * @returns {*} El valor encontrado o una cadena vacía si no se encuentra.
   */
  function getValueByPath(obj, path) {
      return path.split('.').reduce((acc, part) => acc && acc[part] ? acc[part] : '', obj);
  }
  /**
 * Determina dinámicamente el valor de la categoría basado en el tipo de solicitud.
 * @param {object} item - El objeto completo del issue de Jira.
 * @returns {string} El valor de la categoría a mostrar.
 */
  function getDynamicCategory(item) {
      // Obtenemos el tipo de solicitud y la categoría por defecto
      const tipoSolicitud = getValueByPath(item, 'fields.customfield_10010.requestType.name');
      const defaultCategory = getValueByPath(item, 'fields.issuetype.name');
      let dynamicCategory = '';

      // Aplicamos las reglas que definimos
       switch (tipoSolicitud) {
        case 'Solicitudes de compras':
            dynamicCategory = getValueByPath(item, 'fields.customfield_12569.value') || getValueByPath(item, 'fields.customfield_12569');
            break;

        // --- INICIO DE LA CORRECCIÓN ---
        // Se combinan en un solo caso, como identificaste correctamente.
        case 'Solicitudes de modificación de contratos y asesorías legales': 
            dynamicCategory = getValueByPath(item, 'fields.customfield_11317.value') || getValueByPath(item, 'fields.customfield_11317');
            break;
        // --- FIN DE LA CORRECCIÓN ---

        case 'Gestión de supervisión contractual':
            dynamicCategory = getValueByPath(item, 'fields.customfield_13044.value') || getValueByPath(item, 'fields.customfield_13044');
            break;

        case 'Gestión de proveedores':
            dynamicCategory = getValueByPath(item, 'fields.customfield_12658.value') || getValueByPath(item, 'fields.customfield_12658');
            break;

        case 'Carrito de compras':
        case 'Solicitud compra express':
        default:
            return defaultCategory;
    }

      // Si encontramos un campo personalizado pero estaba vacío, usamos el valor por defecto
      return dynamicCategory || defaultCategory;
  }

  // =================================
  //  UI & VIEW MANAGEMENT
  // =================================

  function show(element) { element.classList.remove('u-hidden'); }
  function hide(element) { element.classList.add('u-hidden'); }
  function showVisually(element) { element.classList.remove('u-visually-hidden'); }
  function hideVisually(element) { element.classList.add('u-visually-hidden'); }

  function showBanner(text, showBack) {
    dom.banner.title.textContent = text;
    showBack ? show(dom.banner.backBtn) : hide(dom.banner.backBtn);
    show(dom.banner.container);
  }

  function hideBanner() {
    hide(dom.banner.container);
  }

  function goHome() {
    stopPolling();
    Object.values(dom.views).forEach(hide);
    show(dom.views.home);
    hideBanner();
    resetFilters();
    dom.views.filters.classList.remove('no-sidebar');
  }

  function populateSelect(selectElement, options, valueKey, textKey, placeholder) {
      selectElement.innerHTML = '';
      const placeholderOption = document.createElement('option');
      placeholderOption.value = '';
      placeholderOption.textContent = placeholder;
      selectElement.appendChild(placeholderOption);

      if (options && Array.isArray(options)) {
        options.forEach(opt => {
            const option = document.createElement('option');
            option.value = opt[valueKey];
            option.textContent = opt[textKey];
            selectElement.appendChild(option);
        });
      }
  }

  function resetFilters() {
    if(state.filtersLoaded){
        dom.filters.entidad.value = '';
        populateSelect(dom.filters.estructura, [], '', '', 'Seleccione una entidad primero');
        dom.filters.estructura.disabled = true;
    }

    if (state.currentMode === 'solicitudes') {
        dom.filters.consulta.value = 'Casos';
    } else {
        dom.filters.consulta.value = 'Contratos';
    }

    dom.filters.consulta.disabled = true;

    state.clientSideFilters = {};
    hide(dom.results.clearClientFilterBtn);
    updateFilterIcons();

    clearResults();
  }

  function clearResults() {
    state.rawData = null;
    state.filteredData = [];
    state.pagination.currentPage = 1;
    hideVisually(dom.results.container);
    hideVisually(dom.results.noResultsMsg);
    hideVisually(dom.results.errorMsg);
    hideVisually(dom.filters.volverBtn);
    dom.results.paginationControls.innerHTML = '';
    dom.results.resultsCount.textContent = '';
    hide(dom.debug.container);
  }

 // =================================
//  CORE LOGIC & API CALLS
// =================================

function transformarObjetoDireccion(item) {
  if (!item || typeof item !== 'object' || !Array.isArray(item.attributes)) {
    console.warn('Item inválido o sin atributos, se omitirá en la transformación:', item);
    return null; 
  }

  const findValue = (attributes, attributeId, valueKey = 'displayValue') => {
    const attribute = attributes.find(attr => attr.objectTypeAttributeId === attributeId);
    if (!attribute || !attribute.objectAttributeValues || attribute.objectAttributeValues.length === 0) return '';
    const valueInfo = attribute.objectAttributeValues[0];
    if (valueInfo.referencedObject) {
        return valueKey === 'searchValue' ? valueInfo.referencedObject.objectKey : valueInfo.referencedObject.label;
    }
    return valueInfo[valueKey] || '';
  };

  return {
    "ID": item.id || '',
    "Key": item.objectKey || '',
    "Direccion/Gerencia": item.label || '',
    "Vicepresidencia": findValue(item.attributes, CONFIG.attributeIds.VICEPRESIDENCIA, 'displayValue'),
    "EntidadNombre": findValue(item.attributes, CONFIG.attributeIds.ENTIDAD_EN_DIRECCION, 'displayValue'),
    "EntidadID": findValue(item.attributes, CONFIG.attributeIds.ENTIDAD_EN_DIRECCION, 'searchValue')
  };
}

function applyClientFilters() {
    const activeFilters = Object.entries(state.clientSideFilters)
        .filter(([key, values]) => values.length > 0);

    if (activeFilters.length > 0) {
        state.filteredData = state.rawData.datos.filter(item => {
            return activeFilters.every(([filterKey, filterValues]) => {
                let itemValue;
                if (state.currentMode === 'contratos') {
                    const keyToIdMap = {
                        'id_attr': CONFIG.attributeIds.ID, 'entidad': CONFIG.attributeIds.ENTIDAD, 'direccion_gerencia': CONFIG.attributeIds.DIRECCION_GERENCIA,
                        'proveedor': CONFIG.attributeIds.PROVEEDOR, 'clm_hijo': CONFIG.attributeIds.CLM_HIJO, 'clm_padre': CONFIG.attributeIds.CLM_PADRE
                    };
                    const attributeId = keyToIdMap[filterKey];
                    if (attributeId) itemValue = findAttributeValue(item, attributeId);
                } else { // Solicitudes
                    itemValue = getValueByPath(item, filterKey);
                }
                return filterValues.includes(itemValue);
            });
        });
        show(dom.results.clearClientFilterBtn);
    } else {
        state.filteredData = [...state.rawData.datos];
        hide(dom.results.clearClientFilterBtn);
    }

    state.pagination.currentPage = 1;
    renderTable();
    updateFilterIcons();
}
async function loadInitialFilters() {
    if (state.filtersLoaded) return;
    hide(dom.debug.container);

    try {
        const res = await fetch(CONFIG.apiUrls.getInitialFilters);
        if (!res.ok) throw new Error('No se pudieron cargar los filtros.');
        const data = await res.json();

        const entidades = data.entidades || data.Entidades || [];
        populateSelect(dom.filters.entidad, entidades, 'Key', 'Entidad', 'Seleccione Entidad...');

        const direcciones = data.direcciones || data.Direcciones || [];
        state.allDirecciones = direcciones.map(item => transformarObjetoDireccion(item)).filter(Boolean); 

        state.filtersLoaded = true;
    } catch (err) {
        console.error("Error al cargar filtros:", err);
        dom.filters.entidad.innerHTML = '<option value="">Error al cargar</option>';
        dom.filters.estructura.innerHTML = '<option value="">Error</option>';
    }
}

  function handleEntidadChange() {
      const selectedEntidadKey = dom.filters.entidad.value;
      const selectEstructura = dom.filters.estructura;

      if (!selectedEntidadKey) {
          populateSelect(selectEstructura, [], '', '', 'Seleccione una entidad primero');
          selectEstructura.disabled = true;
          return;
      }

      const filteredDirecciones = state.allDirecciones.filter(dir => dir.EntidadID === selectedEntidadKey);

      populateSelect(selectEstructura, filteredDirecciones, 'Key', 'Direccion/Gerencia', 'Seleccione Dirección...');
      selectEstructura.disabled = false;
  }

  function startPolling() {
    if (state.polling.timer) return;
    state.polling.attempts = 0;
    state.polling.timer = setInterval(checkApproval, state.polling.interval);
    checkApproval();
  }

  function stopPolling() {
    if (state.polling.timer) {
      clearInterval(state.polling.timer);
      state.polling.timer = null;
    }
  }

  async function handleEmailSubmit() {
    const email = dom.emailForm.input.value.trim();
    hideVisually(dom.emailForm.errorMsg);

    if (!email || !email.match(/^[\w.\-]+@([\w\-]+\.)+[\w\-]{2,}$/)) {
      dom.emailForm.errorMsg.textContent = "Ingrese un correo válido.";
      showVisually(dom.emailForm.errorMsg);
      return;
    }

    try {
      const res = await fetch(CONFIG.apiUrls.sendEmail, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email })
      });
      if (!res.ok) throw new Error('No se pudo enviar la solicitud de validación.');

      state.currentUserEmail = email;
      hide(dom.views.email);
      show(dom.views.wait);
      startPolling();
    } catch (err) {
      dom.emailForm.errorMsg.textContent = `Error: ${err.message}`;
      showVisually(dom.emailForm.errorMsg);
    }
  }

  async function checkApproval() {
    state.polling.attempts++;
    if (state.polling.attempts > state.polling.maxAttempts) {
        stopPolling();
        dom.wait.errorMsg.textContent = "El tiempo de espera para la aprobación ha expirado.";
        showVisually(dom.wait.errorMsg);
        showVisually(dom.wait.rejectionHomeBtn);
        return;
    }

    try {
        const url = `${CONFIG.apiUrls.checkApproval}&email=${encodeURIComponent(state.currentUserEmail)}`;

        const res = await fetch(url);
        if (!res.ok) throw new Error('No se pudo verificar el estado.');

        const data = await res.json();
        const estado = (data.aprobado || "").toLowerCase().trim();

        if (estado === "true") {
            stopPolling();
            hide(dom.views.wait);

            if (state.currentMode === 'contratos') {
                showBanner('Contratos Verona', true);
                show(dom.views.cargandoContratos);
                fetchContractsData();
            } else { 
                showBanner('Solicitudes Verona', true);
                show(dom.views.filters);
                updateTableStructure();
                loadInitialFilters();
            }
        } else if (estado === "false") {
            stopPolling();
            dom.wait.errorMsg.textContent = "Su solicitud de acceso ha sido rechazada.";
            showVisually(dom.wait.errorMsg);
            showVisually(dom.wait.rejectionHomeBtn);
        } else if (estado === "pendiente") {
            dom.wait.errorMsg.textContent = "Aún sin aprobación...";
            showVisually(dom.wait.errorMsg);
            hideVisually(dom.wait.rejectionHomeBtn);
        } else {
            console.warn("Estado desconocido recibido:", estado);
        }

    } catch (err) {
        console.error("Error durante el polling:", err);
        stopPolling();
        dom.wait.errorMsg.textContent = "Error de red. No se pudo verificar el estado.";
        showVisually(dom.wait.errorMsg);
        showVisually(dom.wait.rejectionHomeBtn);
    }
  }

  async function fetchContractsData() {
      hide(dom.debug.container);

      const payload = {
          correo: state.currentUserEmail,
          consulta: dom.filters.consulta.value,
          origen: document.referrer || window.location.href
      };

      try {
          const res = await fetch(CONFIG.apiUrls.contratos, {
              method: 'POST',
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify(payload)
          });

          if (!res.ok) throw new Error(`El servidor respondió con estado ${res.status}`);
          const data = await res.json();
          processContractsResponse(data);

      } catch (err) {
          console.error("Error al obtener datos de contratos:", err);
          hide(dom.contracts.spinner);
          dom.contracts.errorMsg.textContent = `Error al obtener los datos. Verifique la consola para más detalles.`;
          showVisually(dom.contracts.errorMsg);
      }
  }

  function processContractsResponse(data) {
      hide(dom.views.cargandoContratos);
      state.rawData = data;
      const profile = (data.perfil || 'user').toLowerCase();
      state.userProfile = profile;

      updateTableStructure();

      const datosParaTabla = Array.isArray(data.datos) ? data.datos : [];

      if (profile === 'admin') {
          dom.views.filters.classList.remove('no-sidebar');
          populateSelect(dom.filters.entidad, data.filtros?.entidades || [], 'Key', 'Entidad', 'Seleccione Entidad...');
          state.allDirecciones = data.filtros?.direcciones || [];
          state.filtersLoaded = true;
          show(dom.views.filters);
          resetFilters();
      } else {
          dom.views.filters.classList.add('no-sidebar');
          state.filteredData = [...datosParaTabla];
          show(dom.views.filters);
          renderTable();
      }
  }


  async function fetchAndRenderData() {
    showVisually(dom.results.loadingMsg);
    hideVisually(dom.results.errorMsg);
    hideVisually(dom.results.container);
    hideVisually(dom.results.noResultsMsg);
    hideVisually(dom.filters.volverBtn);
    hide(dom.results.clearClientFilterBtn);
    hide(dom.debug.container);
    state.rawData = null;
    state.filteredData = [];
    state.clientSideFilters = {};
    state.pagination.currentPage = 1;

    updateTableStructure();

    const selectEntidad = dom.filters.entidad;
    const selectEstructura = dom.filters.estructura;
    const nombreEntidad = selectEntidad.value ? selectEntidad.options[selectEntidad.selectedIndex].text : '';
    const nombreEstructura = selectEstructura.value ? selectEstructura.options[selectEstructura.selectedIndex].text : '';

    const payload = {
        projectKey: dom.filters.project.value,
        entidad: nombreEntidad,
        estructuraNivel: nombreEstructura,
        consulta: dom.filters.consulta.value,
        origen: document.referrer || window.location.href
    };

    const isContratosRequest = dom.filters.consulta.value === 'Contratos';
    const webhookUrl = isContratosRequest ? CONFIG.apiUrls.contratos : CONFIG.apiUrls.solicitudes;

    try {
        const res = await fetch(webhookUrl, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (!res.ok) throw new Error(`El servidor respondió con estado ${res.status}`);

        const data = await res.json();

        if (!Array.isArray(data)) {
            throw new Error('La respuesta del servidor no tiene el formato de lista (array) esperado.');
        }

        state.rawData = { datos: data };
        state.filteredData = [...data];

        renderTable();

    } catch (err) {
        dom.results.errorMsg.textContent = `Error al consultar: ${err.message}`;
        showVisually(dom.results.errorMsg);
        renderTable();
    } finally {
        hideVisually(dom.results.loadingMsg);
    }
}
  // =================================
  //  RENDERING & EXPORT
  // =================================

  function findAttributeValue(item, attributeId) {
      if (!item || !Array.isArray(item.attributes)) return '';
      const attribute = item.attributes.find(attr => attr.objectTypeAttributeId === attributeId);
      if (!attribute || !attribute.objectAttributeValues || attribute.objectAttributeValues.length === 0) return '';

      const valueInfo = attribute.objectAttributeValues[0];
      if (valueInfo.referencedObject) return valueInfo.referencedObject.label;
      if (valueInfo.user) return valueInfo.user.displayName;
      return valueInfo.displayValue;
  }

  function findMultipleAttributeValues(item, attributeId) {
      if (!item || !Array.isArray(item.attributes)) return [];
      const attribute = item.attributes.find(attr => attr.objectTypeAttributeId === attributeId);
      if (!attribute || !attribute.objectAttributeValues || attribute.objectAttributeValues.length === 0) return [];

      return attribute.objectAttributeValues.map(valueInfo => {
          if (valueInfo.referencedObject) return valueInfo.referencedObject.label;
          if (valueInfo.user) return valueInfo.user.displayName;
          return valueInfo.displayValue;
      }).filter(Boolean);
  }

  function createContractDetailCardHtml(item) {
      const attributeMap = {
          [CONFIG.attributeIds.ID]: 'ID', [CONFIG.attributeIds.ENTIDAD]: 'Entidad', [CONFIG.attributeIds.DIRECCION_GERENCIA]: 'Dirección/Gerencia',
          [CONFIG.attributeIds.PROVEEDOR]: 'Proveedor', [CONFIG.attributeIds.CLM_HIJO]: 'CLM_Hijo', [CONFIG.attributeIds.CLM_PADRE]: 'CLM_Padre',
          [CONFIG.attributeIds.TITULO]: 'Título', [CONFIG.attributeIds.OBJETO]: 'Objeto', [CONFIG.attributeIds.ALCANCE]: 'Alcance',
          [CONFIG.attributeIds.ESPECIE]: 'Especie', [CONFIG.attributeIds.GENERO]: 'Genero', [CONFIG.attributeIds.POLIZAS]: 'Polizas',
          [CONFIG.attributeIds.CLAUSULAS]: 'Clausulas', [CONFIG.attributeIds.MATERIALES_UNSPSC]: 'MaterialesUNSPSC', [CONFIG.attributeIds.FECHA_INICIO]: 'Fecha_Inicio',
          [CONFIG.attributeIds.FECHA_FIN]: 'Fecha_Fin', [CONFIG.attributeIds.ADMINISTRADOR]: 'Administrador', [CONFIG.attributeIds.ADMINISTRADOR_SUPLENTE]: 'Administrador_Suplente',
          [CONFIG.attributeIds.TACTICO_1]: 'Tactico_1', [CONFIG.attributeIds.TACTICO_2]: 'Tactico_2', [CONFIG.attributeIds.TACTICO_3]: 'Tactico_3',
          [CONFIG.attributeIds.ES_CONTRATO_MARCO]: 'EsContrato_Marco', [CONFIG.attributeIds.MONTO_COP]: 'Monto en COP', [CONFIG.attributeIds.ES_CONTRATO_CORPORATIVO]: 'EsContrato_Corporativo',
          [CONFIG.attributeIds.CONTRATO_EN_RENOVACION_CSA]: 'Contrato_EnRenovacionPorCSA'
      };
      const multiValueIds = [CONFIG.attributeIds.POLIZAS, CONFIG.attributeIds.CLAUSULAS];
      const placeholder = '-';

      const relevantAttributes = Object.keys(attributeMap).map(id => {
          let valueHtml;
          if (multiValueIds.includes(id)) {
              const values = findMultipleAttributeValues(item, id);
              valueHtml = values.length > 0 ? `<div class="attribute-tags-container">${values.map(v => `<span class="attribute-tag">${v}</span>`).join('')}</div>` : placeholder;
          } else {
              valueHtml = findAttributeValue(item, id) || placeholder;
          }
          return { label: attributeMap[id], value: valueHtml };
      });

      const attributesHtml = relevantAttributes.map(attr => `<li><span class="label">${attr.label}:</span><div class="value">${attr.value}</div></li>`).join('');

      return `<div class="contract-card-wrapper">
              <div class="card-header">
                  <img src="${item.avatar?.url48 || ''}" alt="">
                  <h2>Detalles Completos: ${findAttributeValue(item, CONFIG.attributeIds.CLM_HIJO) || item.label}</h2>
              </div>
              <div class="card-body">
                  <ul class="attribute-list">${attributesHtml}</ul>
                  <p class="card-footnote"><i>* Un guion (-) indica que no hay dato existente para este campo.</i></p>
              </div>
          </div>`;
  }

function createSolicitudDetailCardHtml(item) {
    const fields = item.fields || {};
    let attributesHtml = '';

    // --- INICIO DE LA MODIFICACIÓN ---
    // 1. Obtenemos el enlace web al issue en Jira
    const issueWebLink = getValueByPath(item, 'fields.customfield_10010._links.web');
    let linkButtonHtml = '';
    if (issueWebLink) {
        linkButtonHtml = `<a href="${issueWebLink}" target="_blank" class="view-issue-link">Ver Issue en Jira</a>`;
    }
    // --- FIN DE LA MODIFICACIÓN ---

    // 2. Procesos internos relacionados (issueLinks)
    const issueLinks = fields.issuelinks || [];
    if (issueLinks.length > 0) {
        let linksHtml = issueLinks.map(link => {
            const relatedIssue = link.inwardIssue || link.outwardIssue;
            if (!relatedIssue) return null;

            const issueTypeName = getValueByPath(relatedIssue, 'fields.issuetype.name');
            const typesToOmit = ['Seguimiento de Cumplimiento ANS', 'Iniciativas de eficiencias'];
            if (typesToOmit.includes(issueTypeName)) return null;

            const key = relatedIssue.key;
            const statusName = getValueByPath(relatedIssue, 'fields.status.name');
            const iconUrl = getValueByPath(relatedIssue, 'fields.issuetype.iconUrl');

            return { key, issueType: issueTypeName, status: statusName, iconUrl };
        })
        .filter(Boolean)
        .map(issue => {
            return `
                <span class="related-issue-tag" title="${issue.issueType} - ${issue.status}">
                    <img src="${issue.iconUrl}" alt="${issue.issueType}" class="issue-type-icon">
                    <strong class="issue-key">${issue.key}</strong>
                    <span class="issue-type-name">${issue.issueType}</span>
                    <span class="issue-status-badge">${issue.status}</span>
                </span>`;
        }).join('');

        if (linksHtml) {
            attributesHtml += `
                <li class="full-width-attribute">
                    <span class="label">Procesos internos relacionados:</span>
                    <div class="value"><div class="attribute-tags-container">${linksHtml}</div></div>
                </li>`;
        }
    }

    // 3. Fecha Final ANS y Días Restantes
    const fechaFinalANS = fields.customfield_15118;
    if (fechaFinalANS) {
        const dateObject = new Date(fechaFinalANS);
        const formattedDate = !isNaN(dateObject) 
            ? dateObject.toLocaleDateString('es-ES', { year: 'numeric', month: 'long', day: 'numeric', timeZone: 'UTC' })
            : fechaFinalANS;

        attributesHtml += `<li><span class="label">Fecha Final ANS:</span><div class="value">${formattedDate}</div></li>`;

        const finalStatuses = ['finalizado', 'cancelado'];
        const currentStatus = fields.status ? fields.status.name.toLowerCase() : '';
        if (!finalStatuses.includes(currentStatus) && !isNaN(dateObject)) {
            const today = new Date();
            today.setUTCHours(0, 0, 0, 0); 
            const targetDate = new Date(fechaFinalANS);
            targetDate.setUTCHours(0, 0, 0, 0);
            const diffTime = targetDate - today;
            const diffDays = Math.round(diffTime / (1000 * 60 * 60 * 24));
            let diasRestantesText = '';
            let diasRestantesClass = '';
            if (diffDays > 0) {
                diasRestantesText = `${diffDays} día(s) restante(s)`;
                diasRestantesClass = 'days-remaining-ok';
            } else if (diffDays === 0) {
                diasRestantesText = 'Vence hoy';
                diasRestantesClass = 'days-remaining-due';
            } else {
                diasRestantesText = `Vencido hace ${Math.abs(diffDays)} día(s)`;
                diasRestantesClass = 'days-remaining-overdue';
            }
            attributesHtml += `<li><span class="label">Días restantes para la resolución:</span><div class="value ${diasRestantesClass}">${diasRestantesText}</div></li>`;
        }
    }

    if (attributesHtml === '') {
        attributesHtml = '<li>No hay detalles específicos para mostrar.</li>';
    }

    // Se añade la variable "linkButtonHtml" al final del div del header
    return `<div class="contract-card-wrapper">
                <div class="card-header">
                    <div class="header-content">
                        <img src="${fields.issuetype?.iconUrl || ''}" alt="">
                        <h2>Mas detalles: ${item.key}</h2>
                    </div>
                    ${linkButtonHtml} 
                </div>
                <div class="card-body">
                    <ul class="attribute-list">${attributesHtml}</ul>
                </div>
            </div>`;
}

  function updateTableStructure() {
    dom.results.tableHeader.innerHTML = '';
    let config;
    if (state.currentMode === 'solicitudes') {
        config = CONFIG.solicitudes.mainFields;
    } else {
        config = tableConfigs.contratos;
    }

    const headerRow = document.createElement('tr');
    config.forEach(col => {
        const th = document.createElement('th');
        th.textContent = col.header;
        headerRow.appendChild(th);
    });
    dom.results.tableHeader.appendChild(headerRow);

    createHeaderFilters();
    makeColumnsResizable(dom.results.table);
  }

  function renderTable() {
    dom.results.tableBody.innerHTML = '';
    hide(dom.debug.container);
    updateFilterIcons();

    const dataToRender = state.filteredData;

    if (!dataToRender || dataToRender.length === 0) {
      dom.results.noResultsMsg.textContent = Object.values(state.clientSideFilters).some(arr => arr && arr.length > 0)
          ? 'No hay resultados que coincidan con el filtro aplicado.'
          : 'No hay resultados para esta consulta.';
      showVisually(dom.results.noResultsMsg);
      hideVisually(dom.results.container);
      dom.results.resultsCount.textContent = '';
      if (state.rawData) {
        dom.debug.output.textContent = JSON.stringify(state.rawData, null, 2);
        show(dom.debug.container);
      }
      return;
    }

    showVisually(dom.results.container);
    showVisually(dom.filters.volverBtn);
    hideVisually(dom.results.noResultsMsg);

    const { currentPage } = state.pagination;
    const rowsPerPage = parseInt(dom.results.rowsPerPageSelect.value, 10);
    state.pagination.rowsPerPage = rowsPerPage;

    const totalItems = dataToRender.length;
    const startIndex = (currentPage - 1) * rowsPerPage;
    const endIndex = Math.min(startIndex + rowsPerPage, totalItems);
    const paginatedData = dataToRender.slice(startIndex, endIndex);

    paginatedData.forEach(item => {
      const tr = document.createElement('tr');
      tr.className = 'contract-row';

      if (state.currentMode === 'contratos') {
          tr.dataset.id = item.id;
          const config = tableConfigs.contratos;
          const summaryData = {
              id_attr: findAttributeValue(item, CONFIG.attributeIds.ID),
              entidad: findAttributeValue(item, CONFIG.attributeIds.ENTIDAD),
              direccion_gerencia: findAttributeValue(item, CONFIG.attributeIds.DIRECCION_GERENCIA),
              proveedor: findAttributeValue(item, CONFIG.attributeIds.PROVEEDOR),
              clm_hijo: findAttributeValue(item, CONFIG.attributeIds.CLM_HIJO),
              clm_padre: findAttributeValue(item, CONFIG.attributeIds.CLM_PADRE),
              titulo: findAttributeValue(item, CONFIG.attributeIds.TITULO),
              fecha_inicio: findAttributeValue(item, CONFIG.attributeIds.FECHA_INICIO),
              fecha_fin: findAttributeValue(item, CONFIG.attributeIds.FECHA_FIN),
              monto_cop: findAttributeValue(item, CONFIG.attributeIds.MONTO_COP),
          };
          config.forEach(col => {
              const td = document.createElement('td');
              td.textContent = summaryData[col.dataKey] || '';
              tr.appendChild(td);
          });
      } else { // Solicitudes
          tr.dataset.id = item.key;
          const config = CONFIG.solicitudes.mainFields;
          config.forEach(col => {
    const td = document.createElement('td');

    if (state.currentMode === 'solicitudes') {
        if (col.header === 'Categoría') {
            td.textContent = getDynamicCategory(item);
        } else if (col.header === 'Fecha Creación') {
            const dateValue = getValueByPath(item, col.jsonPath);
            td.textContent = dateValue 
                ? new Date(dateValue).toLocaleString('es-CO', { timeZone: 'America/Bogota' }) 
                : '';
        } else {
            td.textContent = getValueByPath(item, col.jsonPath);
        }
    } else {
        td.textContent = getValueByPath(item, col.jsonPath);
    }

    tr.appendChild(td);
});
      }
      dom.results.tableBody.appendChild(tr);
    });

    dom.results.resultsCount.textContent = `Mostrando ${startIndex + 1}-${endIndex} de ${totalItems} resultados`;
    renderPaginationControls(totalItems);
  }

  function renderPaginationControls(totalItems) {
      const { currentPage, rowsPerPage } = state.pagination;
      const totalPages = Math.ceil(totalItems / rowsPerPage);

      dom.results.paginationControls.innerHTML = '';
      if(totalPages <= 1) return;

      const prevBtn = document.createElement('button');
      prevBtn.innerHTML = '&lt;';
      prevBtn.disabled = currentPage === 1;
      prevBtn.addEventListener('click', () => {
          if(currentPage > 1) {
              state.pagination.currentPage--;
              renderTable();
          }
      });
      dom.results.paginationControls.appendChild(prevBtn);

      const pageInfo = document.createElement('span');
      pageInfo.className = 'page-info';
      pageInfo.textContent = `Página ${currentPage} de ${totalPages}`;
      dom.results.paginationControls.appendChild(pageInfo);

      const nextBtn = document.createElement('button');
      nextBtn.innerHTML = '&gt;';
      nextBtn.disabled = currentPage === totalPages;
      nextBtn.addEventListener('click', () => {
          if(currentPage < totalPages) {
              state.pagination.currentPage++;
              renderTable();
          }
      });
      dom.results.paginationControls.appendChild(nextBtn);
  }

  // === INICIO DEL CÓDIGO CORREGIDO PARA EXPORTAR TODO ===

    /**
     * Helper para formatear valores CSV, escapando comillas y delimitadores.
     * @param {*} value - El valor a formatear.
     * @returns {string} El valor formateado.
     */
    function formatCsvValue(value) {
        if (value === null || typeof value === 'undefined') {
            return '';
        }
        let strValue = String(value);
        strValue = strValue.replace(/"/g, '""'); // Escapar comillas dobles
        if (strValue.includes(',') || strValue.includes('"') || strValue.includes('\n')) {
            return `"${strValue}"`;
        }
        return strValue;
    }
    
    /**
     * Helper para formatear fechas en DD/MM/YYYY.
     * @param {string} dateString - Cadena de fecha ISO 8601.
     * @returns {string} Fecha formateada o la cadena original si es inválida.
     */
    function formatDateToDDMMYYYY(dateString) {
        if (!dateString) return '';
        const date = new Date(dateString);
        if (isNaN(date)) return dateString;
        const day = String(date.getDate()).padStart(2, '0');
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const year = date.getFullYear();
        return `${day}/${month}/${year}`;
    }

    function exportCsv() {
        if (!state.filteredData || state.filteredData.length === 0) {
            alert('No hay datos para exportar.');
            return;
        }

        let headers;
        let rows;
        let fileName;
        const multiValueIds = [CONFIG.attributeIds.POLIZAS, CONFIG.attributeIds.CLAUSULAS];

        if (state.currentMode === 'contratos') {
            fileName = 'export_contratos.csv';
            // Se crea un mapa de IDs de atributos a nombres de cabecera
            const attributeMap = {
                [CONFIG.attributeIds.ID]: 'ID', [CONFIG.attributeIds.ENTIDAD]: 'Entidad', [CONFIG.attributeIds.DIRECCION_GERENCIA]: 'Dirección/Gerencia',
                [CONFIG.attributeIds.PROVEEDOR]: 'Proveedor', [CONFIG.attributeIds.CLM_HIJO]: 'CLM_Hijo', [CONFIG.attributeIds.CLM_PADRE]: 'CLM_Padre',
                [CONFIG.attributeIds.TITULO]: 'Título', [CONFIG.attributeIds.OBJETO]: 'Objeto', [CONFIG.attributeIds.ALCANCE]: 'Alcance',
                [CONFIG.attributeIds.ESPECIE]: 'Especie', [CONFIG.attributeIds.GENERO]: 'Genero', [CONFIG.attributeIds.POLIZAS]: 'Pólizas',
                [CONFIG.attributeIds.CLAUSULAS]: 'Cláusulas', [CONFIG.attributeIds.MATERIALES_UNSPSC]: 'Materiales UNSPSC', [CONFIG.attributeIds.FECHA_INICIO]: 'Fecha Inicio',
                [CONFIG.attributeIds.FECHA_FIN]: 'Fecha Fin', [CONFIG.attributeIds.ADMINISTRADOR]: 'Administrador', [CONFIG.attributeIds.ADMINISTRADOR_SUPLENTE]: 'Administrador Suplente',
                [CONFIG.attributeIds.TACTICO_1]: 'Táctico 1', [CONFIG.attributeIds.TACTICO_2]: 'Táctico 2', [CONFIG.attributeIds.TACTICO_3]: 'Táctico 3',
                [CONFIG.attributeIds.ES_CONTRATO_MARCO]: 'Es Contrato Marco', [CONFIG.attributeIds.MONTO_COP]: 'Monto en COP', [CONFIG.attributeIds.ES_CONTRATO_CORPORATIVO]: 'Es Contrato Corporativo',
                [CONFIG.attributeIds.CONTRATO_EN_RENOVACION_CSA]: 'Contrato en Renovación por CSA'
            };
            headers = Object.values(attributeMap);

            rows = state.filteredData.map(item => {
                return Object.keys(attributeMap).map(id => {
                    let value;
                    if (multiValueIds.includes(id)) {
                        value = findMultipleAttributeValues(item, id).join('; ');
                    } else {
                        value = findAttributeValue(item, id);
                    }
                    if (id === CONFIG.attributeIds.FECHA_INICIO || id === CONFIG.attributeIds.FECHA_FIN) {
                        return formatDateToDDMMYYYY(value);
                    }
                    return value;
                });
            });
        } else if (state.currentMode === 'solicitudes') {
            fileName = 'export_solicitudes.csv';
            headers = [
                'Key', 'Summary', 'Status', 'Asignado', 'Reportado por', 'Fecha Creación', 'Tipo de Solicitud', 'Categoría Dinámica',
                'Procesos Internos Relacionados', 'Fecha Final ANS', 'Días Restantes', 'Link a Jira'
            ];
            rows = state.filteredData.map(item => {
                const fechaCreacionRaw = getValueByPath(item, 'fields.created');
                const fechaCreacionFormatted = fechaCreacionRaw ?
                    new Date(fechaCreacionRaw).toLocaleString('es-CO', { timeZone: 'America/Bogota' }) : '';
                const issueLinks = (item.fields.issuelinks || [])
                    .map(link => (link.inwardIssue || link.outwardIssue)?.key)
                    .filter(Boolean)
                    .join('; ');
                const fechaFinalANS = item.fields.customfield_15118;
                let diasRestantesText = '';
                if (fechaFinalANS) {
                    const finalStatuses = ['finalizado', 'cancelado'];
                    const currentStatus = (item.fields.status?.name || '').toLowerCase();
                    if (!finalStatuses.includes(currentStatus)) {
                        const diffDays = Math.round((new Date(fechaFinalANS) - new Date()) / (1000 * 60 * 60 * 24));
                        diasRestantesText = diffDays > 0 ? `${diffDays} restantes` : (diffDays === 0 ? 'Vence hoy' : `Vencido hace ${Math.abs(diffDays)}`);
                    }
                }
                return [
                    item.key,
                    item.fields.summary,
                    item.fields.status?.name,
                    item.fields.assignee?.emailAddress,
                    item.fields.creator?.displayName,
                    fechaCreacionFormatted,
                    getValueByPath(item, 'fields.customfield_10010.requestType.name'),
                    getDynamicCategory(item),
                    issueLinks,
                    formatDateToDDMMYYYY(fechaFinalANS),
                    diasRestantesText,
                    getValueByPath(item, 'fields.customfield_10010._links.web') || ''
                ];
            });
        } else {
            alert('No se pudo determinar el tipo de datos a exportar.');
            return;
        }

        const csvContent = headers.map(formatCsvValue).join(',') + '\n' +
                           rows.map(row => row.map(formatCsvValue).join(',')).join('\n');

        const encodedUri = encodeURI(`data:text/csv;charset=utf-8,${csvContent}`);
        const link = document.createElement("a");
        link.setAttribute("href", encodedUri);
        link.setAttribute("download", fileName);
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    }
  // === FIN DEL CÓDIGO CORREGIDO ===

  function makeColumnsResizable(table) {
    const headers = table.querySelectorAll('th');
    headers.forEach(th => {
        if (th.querySelector('.resize-handle')) return;
        const handle = document.createElement('div');
        handle.className = 'resize-handle';
        th.appendChild(handle);

        handle.addEventListener('mousedown', (e) => {
            e.preventDefault();
            e.stopPropagation();
            const startX = e.pageX;
            const startWidth = th.offsetWidth;

            const handleMouseMove = (moveEvent) => {
                const newWidth = startWidth + (moveEvent.pageX - startX);
                if (newWidth > 40) {
                    th.style.width = `${newWidth}px`;
                }
            };

            const handleMouseUp = () => {
                document.removeEventListener('mousemove', handleMouseMove);
                document.removeEventListener('mouseup', handleMouseUp);
            };

            document.addEventListener('mousemove', handleMouseMove);
            document.addEventListener('mouseup', handleMouseUp);
        });
    });
  }

  function createHeaderFilters() {
      let config;
      if (state.currentMode === 'solicitudes') {
          config = CONFIG.solicitudes.mainFields;
      } else {
          config = tableConfigs.contratos;
      }
      const headers = dom.results.tableHeader.querySelectorAll('th');

      headers.forEach(th => {
          const headerText = th.textContent.trim();
          const column = config.find(c => c.header === headerText);

          if (column && column.isFilterable) {
              const dataKey = state.currentMode === 'solicitudes' ? column.jsonPath : column.dataKey;
              th.innerHTML = `
                  <div class="th-content">
                      <span>${column.header}</span>
                      <span class="filter-icon" data-filter-key="${dataKey}">
                          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" viewBox="0 0 16 16"><path d="M6 10.5a.5.5 0 0 1 .5-.5h3a.5.5 0 0 1 0 1h-3a.5.5 0 0 1-.5-.5zm-2-3a.5.5 0 0 1 .5-.5h7a.5.5 0 0 1 0 1h-7a.5.5 0 0 1-.5-.5zm-2-3a.5.5 0 0 1 .5-.5h11a.5.5 0 0 1 0 1h-11a.5.5 0 0 1-.5-.5z"/></svg>
                      </span>
                  </div>`;
          } else {
            th.innerHTML = `<span>${headerText}</span>`;
          }
      });
  }

      function openFilterDropdown(e) {
        const icon = e.target.closest('.filter-icon');
        if (!icon) return;

        e.stopPropagation();
        closeAllDropdowns();

        const filterKey = icon.dataset.filterKey;
        const th = icon.closest('th');

        const dropdown = document.createElement('div');
        dropdown.className = 'filter-dropdown';

        const uniqueValues = [...new Set(
            state.rawData.datos
                .map(item => String(getValueByPath(item, filterKey) || ''))
                .filter(Boolean)
        )].sort();

        const list = document.createElement('ul');
        list.className = 'filter-dropdown-list';

        uniqueValues.forEach(value => {
            const li = document.createElement('li');
            const label = document.createElement('label');
            const checkbox = document.createElement('input');
            checkbox.type = 'checkbox';
            checkbox.value = value;
            checkbox.checked = (state.clientSideFilters[filterKey] || []).includes(value);
            label.appendChild(checkbox);
            label.appendChild(document.createTextNode(` ${value}`));
            li.appendChild(label);
            list.appendChild(li);
        });
        dropdown.appendChild(list);

        const actions = document.createElement('div');
        actions.className = 'filter-dropdown-actions';
        const applyBtn = document.createElement('button');
        applyBtn.textContent = 'Aplicar';
        const clearBtn = document.createElement('button');
        clearBtn.textContent = 'Limpiar';

        applyBtn.onclick = () => {
            const selectedValues = Array.from(list.querySelectorAll('input:checked')).map(cb => cb.value);
            state.clientSideFilters[filterKey] = selectedValues;
            applyClientFilters();
            closeAllDropdowns();
        };

        clearBtn.onclick = () => {
            state.clientSideFilters[filterKey] = [];
            applyClientFilters();
            closeAllDropdowns();
        };

        actions.appendChild(clearBtn);
        actions.appendChild(applyBtn);
        dropdown.appendChild(actions);

        document.body.appendChild(dropdown);
        const rect = th.getBoundingClientRect();
        dropdown.style.position = 'absolute';
        dropdown.style.top = `${rect.bottom + window.scrollY}px`;
        dropdown.style.left = `${rect.left + window.scrollX}px`;
    }

  function closeAllDropdowns(e) {
      if (e && (e.target.closest('.filter-dropdown') || e.target.closest('.filter-icon'))) return;
      document.querySelectorAll('.filter-dropdown').forEach(d => d.remove());
  }

  function updateFilterIcons() {
      dom.results.tableHeader.querySelectorAll('.filter-icon').forEach(icon => {
          const key = icon.dataset.filterKey;
          if (state.clientSideFilters[key] && state.clientSideFilters[key].length > 0) {
              icon.classList.add('active');
          } else {
              icon.classList.remove('active');
          }
      });
  }


  // =================================
  //  EVENT BINDING
  // =================================

  function bindEventListeners() {
    dom.home.goSolicitudesBtn.addEventListener('click', () => {
      state.currentMode = "solicitudes";
      hide(dom.views.home);
      showBanner('Solicitudes Verona', true); 
      show(dom.views.filters);
      dom.filters.consulta.value = 'Casos';
      dom.filters.consulta.disabled = true;
      updateTableStructure();
      loadInitialFilters();
    });

    dom.home.goContratosBtn.addEventListener('click', () => {
      state.currentMode = "contratos";
      hide(dom.views.home);
      showBanner('Contratos Verona', true);
      show(dom.views.filters);
      dom.filters.consulta.value = 'Contratos';
      dom.filters.consulta.disabled = true;
      updateTableStructure();
      loadInitialFilters();
    });

    dom.banner.backBtn.addEventListener('click', goHome);
    dom.emailForm.submitBtn.addEventListener('click', handleEmailSubmit);
    dom.wait.rejectionHomeBtn.addEventListener('click', goHome);

    dom.filters.consultarBtn.addEventListener('click', fetchAndRenderData);
    dom.filters.resetBtn.addEventListener('click', resetFilters);
    dom.filters.volverBtn.addEventListener('click', clearResults);

    dom.results.exportBtn.addEventListener('click', exportCsv);
    dom.results.rowsPerPageSelect.addEventListener('change', () => {
        state.pagination.currentPage = 1;
        renderTable();
    });

    dom.filters.entidad.addEventListener('change', handleEntidadChange);

    dom.results.clearClientFilterBtn.addEventListener('click', () => {
        state.clientSideFilters = {};
        applyClientFilters();
    });

    dom.results.tableHeader.addEventListener('click', openFilterDropdown);
    document.addEventListener('click', closeAllDropdowns);

    dom.results.tableBody.addEventListener('click', e => {
        const row = e.target.closest('tr.contract-row');
        if (!row) return;

        const existingDetailRow = row.nextElementSibling;
        if (existingDetailRow && existingDetailRow.classList.contains('detail-row')) {
            existingDetailRow.remove();
            row.classList.remove('is-expanded');
            return;
        }

        document.querySelectorAll('tr.detail-row').forEach(detail => detail.remove());
        document.querySelectorAll('tr.contract-row.is-expanded').forEach(r => r.classList.remove('is-expanded'));

        const itemId = row.dataset.id;
        const sourceData = (state.rawData && Array.isArray(state.rawData.datos)) ? state.rawData.datos : state.filteredData;

        let itemData;
        let detailHtml = '';

        if (state.currentMode === 'contratos') {
            itemData = sourceData.find(d => d.id === itemId);
            if(itemData) detailHtml = createContractDetailCardHtml(itemData);
        } else { // Solicitudes
            itemData = sourceData.find(d => d.key === itemId);
            if(itemData) detailHtml = createSolicitudDetailCardHtml(itemData);
        }

        if (itemData) {
          row.classList.add('is-expanded');
          const detailRow = document.createElement('tr');
          detailRow.className = 'detail-row';
          const cell = document.createElement('td');
          cell.colSpan = row.cells.length;
          cell.innerHTML = detailHtml;
          detailRow.appendChild(cell);
          row.insertAdjacentElement('afterend', detailRow);
        }
    });
  }

  // =================================
  //  INITIALIZATION
  // =================================

  bindEventListeners();
  dom.filters.project.value = 'CSA';

});
</script>

</body>
</html>