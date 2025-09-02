function openFilterDropdown(e) {
    const icon = e.target.closest('.filter-icon');
    if (!icon) return;

    e.stopPropagation();
    closeAllDropdowns();

    const filterKey = icon.dataset.filterKey;
    const th = icon.closest('th');

    const dropdown = document.createElement('div');
    dropdown.className = 'filter-dropdown';

    // --- INICIO DE LA CORRECCIÓN ---
    let uniqueValues = [];
    if (state.currentMode === 'contratos') {
        // Mapeo de la clave de datos al ID de atributo, necesario para contratos
        const keyToIdMap = {
            'id_attr': CONFIG.attributeIds.ID,
            'entidad': CONFIG.attributeIds.ENTIDAD,
            'direccion_gerencia': CONFIG.attributeIds.DIRECCION_GERENCIA,
            'proveedor': CONFIG.attributeIds.PROVEEDOR,
            'clm_hijo': CONFIG.attributeIds.CLM_HIJO,
            'clm_padre': CONFIG.attributeIds.CLM_PADRE
        };
        const attributeId = keyToIdMap[filterKey];
        if (attributeId) {
            uniqueValues = [...new Set(
                state.rawData.datos
                    .map(item => String(findAttributeValue(item, attributeId) || ''))
                    .filter(Boolean)
            )].sort();
        }
    } else { // El comportamiento original para 'solicitudes' se mantiene
        uniqueValues = [...new Set(
            state.rawData.datos
                .map(item => String(getValueByPath(item, filterKey) || ''))
                .filter(Boolean)
        )].sort();
    }
    // --- FIN DE LA CORRECCIÓN ---

    if (uniqueValues.length === 0) {
        // Opcional: podrías mostrar un pequeño mensaje si no hay nada que filtrar
        console.warn(`No hay valores únicos para filtrar en la columna con clave: ${filterKey}`);
        return; 
    }

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
