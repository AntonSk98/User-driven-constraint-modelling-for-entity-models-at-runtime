async function copyAttribute(path) {
    await copyPath(path, 'attribute');
}

async function copyNavigation(path) {
    await copyPath(path, 'association');
}

async function copyPath(path, type) {
    const successNotification = getSuccessNotification();
    const errorNotification = getErrorNotification();
    try {
        await navigator.clipboard.writeText(path);
        successNotification({message: `Successfully copied ${type} into clipboard!`})
    } catch (e) {
        errorNotification({message: `Error occurred while copying ${type}}`});
    }
}

function prettyPrint() {
    const errorNotification = getErrorNotification()
    let input = document.getElementById('constrain_space').value;
    try {
        let formatted = JSON.parse(input);
        document.getElementById('constrain_space').value = JSON.stringify(formatted, undefined, 2);
    } catch (err) {
        const text = "" + err;
        errorNotification({
            message: text
        });
        throw err;
    }
}

async function addConstraintFunction(functionName) {
    const errorNotification = getErrorNotification();
    try {
        let constrainSpace = document.getElementById('constrain_space');
        let currentPosition = constrainSpace.selectionStart;
        let currentTemplate = constrainSpace.value;
        let response = await fetch('/get_template?' + new URLSearchParams({
            name: functionName
        }));
        if (response.ok) {
            const template = await response.json();
            const constraintTemplate = template.template;
            const updatedConstraint = currentTemplate.slice(0, currentPosition) + constraintTemplate + currentTemplate.slice(currentPosition);
            constrainSpace.value = JSON.stringify(JSON.parse(updatedConstraint), undefined, 2);
        } else {
            const text = "Something went wrong while fetching a constraint's template";
            errorNotification({
                message: text
            });
        }
    } catch (err) {
        const text = 'Please replace placeholders with concrete values by following the predefined hints!';
        errorNotification({
            message: text
        });
        throw err;
    }
}

async function resetFunctionTemplates() {
    const errorNotification = getErrorNotification();
    const successNotification = getSuccessNotification();

    const response = await fetch('/reset_function_templates');
    if (response.ok) {
        successNotification({message: 'Successfully reset all template functions!'})
        setTimeout(() => location.reload(), 1500)
    } else {
        errorNotification({message: 'Error occurred while resetting template functions!'})
    }
}

async function saveConstraint(typeName) {
    const errorNotification = getErrorNotification();
    const successNotification = getSuccessNotification();

    let response = await fetch('/persist_constraint?' + new URLSearchParams({
        typeName: typeName
    }), {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(JSON.parse(document.getElementById('constrain_space').value))
    });

    if (response.ok) {
        successNotification({message: 'Successfully saved a constraint!'})
        setTimeout(() => location.reload(), 3000)
    } else {
        const error = await response.json();
        errorNotification({message: error.message})
    }
}

async function addToOpenedModelElement(from, to, path) {
    const errorNotification = getErrorNotification();
    console.log(path);

    const response = await fetch("/add_to_opened_model_element?" + new URLSearchParams({
        from: from,
        to: to,
        path: path
    }));

    if (!response.ok) {
        errorNotification({message: `Error occurred while expanding ${from} with ${name} in the current view`});
    }

    const modelElement = await response.json();
    const associations = modelElement.associations;
    const attributes = modelElement.attributes;

    for (let i = 0; i < attributes.length; i++) {
        createNewAttributeRow(attributes[i]);
    }

    removeOnClickFunction(document.getElementById(path));

    for (let i = 0; i < associations.length; i++) {
        createNewAssociationRow(modelElement.name, associations[i]);
    }

}

function removeOnClickFunction(associationReference) {
    associationReference.type = 'text'
    associationReference.removeAttribute("onclick")
    associationReference.classList.add('target-type-button-no-decoration')
    associationReference.classList.remove('target-type-button-hover')
    associationReference.readOnly = true;
}

function createCopyButton(buttonName, valueToCopy) {
    const copyButton = document.createElement('input');
    copyButton.type = 'button';
    copyButton.value = buttonName;
    copyButton.classList.add('copy-button')
    copyButton.setAttribute('onclick', `copyPath('${valueToCopy}')`)
    return copyButton
}

function createNewAttributeRow(attribute) {
    let lastAttributeRow = document.getElementById('attribute-body').querySelector("tr:last-child");
    const newAttributeRow = document.createElement('tr');

    const keyData = document.createElement('td');
    const datatypeData = document.createElement('td');
    const pathData = document.createElement('td');
    const keyDiv = document.createElement('div');
    const datatypeDiv = document.createElement('div');
    const pathCopyDiv = document.createElement('div');
    const attributePathDiv = document.createElement('div');


    keyDiv.className = datatypeDiv.className = pathCopyDiv.className = 'restricted-width';
    attributePathDiv.className = 'attribute-path'
    keyDiv.title = attribute.key;

    const pathToCopyNavigation = createCopyButton('Navigation', attribute.navigation);
    const pathToCopyAttribute = createCopyButton('Attribute', attribute.attribute)


    keyDiv.appendChild(document.createTextNode(attribute.key));
    datatypeDiv.appendChild(document.createTextNode(attribute.datatype));
    attributePathDiv.appendChild(pathToCopyNavigation);
    attributePathDiv.appendChild(pathToCopyAttribute);
    pathCopyDiv.appendChild(attributePathDiv);

    keyData.appendChild(keyDiv);
    datatypeData.appendChild(datatypeDiv);
    pathData.appendChild(pathCopyDiv);
    newAttributeRow.appendChild(keyData);
    newAttributeRow.appendChild(datatypeData);
    newAttributeRow.appendChild(pathData)
    lastAttributeRow.parentNode.insertBefore(newAttributeRow, lastAttributeRow.nextSibling);
}

function createNewAssociationRow(from, association) {
    const lastAssociationRow = document.getElementById('association-body').querySelector("tr:last-child");
    const newAssociationRow = document.createElement('tr');
    const sourceDiv = document.createElement('div');
    const byRelationDiv = document.createElement('div');
    const targetDiv = document.createElement('div');
    const copyPathDiv = document.createElement('div');
    sourceDiv.className = byRelationDiv.className = targetDiv.className = copyPathDiv.className = 'restricted-width';
    sourceDiv.title = from;


    const targetInput = document.createElement('input');
    targetInput.type = 'button';
    targetInput.value = targetInput.title = association.targetModelElementName;
    targetInput.id = association.navigation;
    targetInput.classList.add('target-type-button');
    targetInput.classList.add('target-type-button-hover');
    targetInput.setAttribute('onclick', `addToOpenedModelElement('${from}','${association.targetModelElementName}', '${association.navigation}')`);

    const pathCopyButton = createCopyButton('Copy', association.navigation);

    sourceDiv.appendChild(document.createTextNode(from));
    byRelationDiv.appendChild(document.createTextNode(association.name));
    targetDiv.appendChild(targetInput);
    copyPathDiv.appendChild(pathCopyButton);

    newAssociationRow.appendChild(document.createElement('td')).appendChild(sourceDiv);
    newAssociationRow.appendChild(document.createElement('td')).appendChild(byRelationDiv);
    newAssociationRow.appendChild(document.createElement('td')).appendChild(targetDiv);
    newAssociationRow.appendChild(document.createElement('td')).appendChild(pathCopyButton);
    lastAssociationRow.parentNode.insertBefore(newAssociationRow, lastAssociationRow.nextSibling);
}

async function updateAttribute(element, id, type, attributeId) {
    const errorNotification = getErrorNotification();
    const successNotification = getSuccessNotification();

    const attributeDataElement = element.parentElement.parentElement.parentElement.getElementsByTagName('td');
    const key = attributeDataElement[0].getElementsByTagName('input')[0].value;
    const datatype = attributeDataElement[1].getElementsByTagName('input')[0].value;

    const response = await fetch("/update_attribute?" + new URLSearchParams({
        id: id,
        type: type,
        attributeId: attributeId,
        key: key,
        datatype: datatype
    }));

    if (!response.ok) {
        errorNotification({message: 'Unexpected error occurred while updating an attribute!'});
    } else {
        successNotification({message: 'Successfully updated the attribute!'});
        setTimeout(() => location.reload(), 2000);
    }
}

async function updateAssociation(element, id, type, associationId) {
    const errorNotification = getErrorNotification();
    const successNotification = getSuccessNotification();

    const associationDataElements = element.parentElement.parentElement.getElementsByTagName('td');
    const byRelationValue = associationDataElements[1].getElementsByTagName('input')[0].value;
    const targetValue = associationDataElements[2].getElementsByTagName('input')[0].value;
    const response = await fetch("/update_association?" + new URLSearchParams({
        id: id,
        type: type,
        associationId: associationId,
        byRelation: byRelationValue,
        target: targetValue
    }));

    if (!response.ok) {
        errorNotification({message: 'Unexpected error occurred while updating an association'});
    } else {
        successNotification({message: 'Successfully updated the association!'});
        setTimeout(() => location.reload(), 2000);
    }
}

async function addRuntimeFunction() {
    const result = await fetch('/get_runtime_function_template');
    if (result.ok) {
        const runtimeFunctionTemplate = await result.json();
        const textarea = document.getElementById('runtime-function-textarea');
        toggleInputs(true);
        blurMainElement(true);
        toggleFunctionDefinitionPopup(true);
        console.log(runtimeFunctionTemplate)
        textarea.value = JSON.stringify(JSON.parse(runtimeFunctionTemplate.template), undefined, 2);
    }
}

async function saveRuntimeFunction() {
    const errorNotification = getErrorNotification();
    const successNotification = getSuccessNotification();

    let response = await fetch('/save_runtime_function',
        {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(JSON.parse(document.getElementById('runtime-function-textarea').value))
        });

    if (response.ok) {
        successNotification({message: 'Successfully added a new function type!'})
        setTimeout(() => location.reload(), 1500)
    } else {
        errorNotification({message: 'Error occurred while adding a new function type!'})
    }
}

function getErrorNotification() {
    return window.createNotification({
        theme: 'error',
        showDuration: 3000,
        closeOnClick: true
    })
}

function getSuccessNotification() {
    return window.createNotification({
        theme: 'success',
        showDuration: 3000,
        closeOnClick: true
    })
}

function closeDetailsPopup() {
    toggleInputs(false);
    blurMainElement(false);
    toggleFunctionDefinitionPopup(false)
}

function toggleInputs(disabled) {
    const inputs = document.getElementsByTagName('input');
    for (let index = 0; index < inputs.length; index++) {
        inputs[index].disabled = disabled;
    }
}

function toggleFunctionDefinitionPopup(showWindow) {
    const instanceDetails = document.getElementById('runtime-function');
    showWindow ? instanceDetails.style.display = 'block' : instanceDetails.style.display = 'none';
}

function blurMainElement(shouldBlur) {
    const main = document.getElementsByTagName('main').item(0);
    shouldBlur ? main.style.filter = 'blur(3px)' : main.style.filter = 'none';
}
