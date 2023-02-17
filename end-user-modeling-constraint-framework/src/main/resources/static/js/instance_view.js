async function createInstance(elementName) {
    const successNotification = getSuccessNotification();
    const errorNotification = getErrorNotification();

    const attributeKeys = Array.from(document.querySelectorAll('[attribute-key]')).map(element => element.innerText);
    const attributeValues = Array.from(document.querySelectorAll('[attribute-value]')).map(element => element.value);
    const associationNames = Array.from(document.querySelectorAll('[association-name]')).map(element => element.innerText);
    const associationValues = Array.from(document.querySelectorAll('[association-value]')).map(element => element.value);

    const attributes = attributeKeys.map((key, i) => ({key: key, value: attributeValues[i]}))
    const associations = associationNames.map((key, i) => ({name: key, targetInstanceUuid: associationValues[i]}))

    const instanceElement = {
        instanceOf: elementName,
        slots: attributes,
        links: associations
    }

    const response = await fetch("/instantiate_element", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(instanceElement)
    });

    if (response.ok) {
        successNotification({message: "Successfully created a new instance"});
        setTimeout(() => location.replace("/"), 1500);
    } else {
        errorNotification({message: "Unexpected error occurred while creating an instance"});
    }
}

async function updateInstance(instanceId) {
    const successNotification = getSuccessNotification();
    const errorNotification = getErrorNotification();

    const slotKeys = Array.from(document.querySelectorAll('[slot-key]')).map(element => element.innerText);
    const slotValues = Array.from(document.querySelectorAll('[slot-value]')).map(element => element.value);
    const linkNames = Array.from(document.querySelectorAll('[link-name]')).map(element => element.innerText);
    const linkValues = Array.from(document.querySelectorAll('[link-value]')).map(element => element.value);

    const slots = slotKeys.map((key, i) => ({key: key, value: slotValues[i]}));
    const links = linkNames.map((key, i) => ({name: key, targetInstanceUuid: linkValues[i]}));

    const instanceElement = {
        uuid: instanceId,
        slots: slots,
        links: links
    };

    const response = await fetch("/update_instance_element", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(instanceElement)
    });

    if (response.ok) {
        successNotification({message: "Successfully updated an instance"});
        setTimeout(() => location.replace("/"), 1500);
    } else {
        errorNotification({message: "Unexpected error occurred while updating an instance"});
    }
}

function newAssociation(element) {
    const focusRow = element.parentElement.parentElement;
    const newRow = focusRow.cloneNode(true);
    Array.from(newRow.getElementsByTagName('input')).forEach(element => element.value = '')
    focusRow.parentNode.insertBefore(newRow, focusRow.nextSibling);
    focusRow.getElementsByClassName("add-button")[0].remove();
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