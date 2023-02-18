let modalView;

window.onload = () => {
    modalView = spawnModal();
    return modalView;
}

async function deleteInstanceById(uuid) {
    const result = await fetch('/delete_instance_by_id?' + new URLSearchParams({uuid: uuid}));
    const errorNotification = getErrorNotification();
    if (result.ok) {
        location.reload();
    } else {
        errorNotification({message: "Unexpected error occurred while deleting an instance"});
    }
}

async function displayInstanceDetails(instanceId) {
    const errorNotification = getErrorNotification();
    const result = await fetch('/get_instance_by_id?' + new URLSearchParams({uuid: instanceId}))
    if (result.ok) {
        const instance = await result.json();
        renderInstanceDetails(instance);
    } else {
        errorNotification({message: "Unexpected error occurred while fetching instance details"});
    }
}

async function displayConstraintById(instanceId, constraintId) {
    const errorNotification = getErrorNotification();
    const result = await fetch('/get_constraint_by_id?' + new URLSearchParams({uuid: constraintId}))
    if (result.ok) {
        const constraint = await result.json();
        renderConstraintDetails(instanceId, constraint);
    } else {
        errorNotification({message: "Unexpected error occurred while getting constraint details"});
    }
}

async function validateConstraint() {
    const errorNotification = getErrorNotification();
    const textarea = document.getElementById('constraint-details-textarea');
    const constraint = JSON.parse(textarea.value);
    const constraintUuid = constraint.uuid;
    const result = await fetch('validate_constraint_by_id?' + new URLSearchParams({
        instanceUuid: document.getElementById('instance-id').value,
        constraintUuid: constraintUuid,

    }));
    if (result.ok) {
        const validationReport = await result.json();
        renderValidationReport(validationReport)
    } else {
        errorNotification({message: "Unexpected error occurred while validating a constraint"});
    }
}

async function removeConstraint() {
    const errorNotification = getErrorNotification();
    const textarea = document.getElementById('constraint-details-textarea');
    const constraint = JSON.parse(textarea.value);
    const constraintUuid = constraint.uuid;
    const result = await fetch('/remove_constraint_by_id?' + new URLSearchParams({uuid: constraintUuid}))
    if (result.ok) {
        location.reload();
    } else {
        errorNotification({message: "Unexpected error occurred while deleting a constraint"})
    }
}


function renderValidationReport(validationReport) {
    closeModal();
    const modalBody = document.getElementById('modal-body');
    const validationReportElement = document.getElementById('validation-report-template').cloneNode(true);
    modalBody.appendChild(validationReportElement.content);
    openModal();

    const validationReportHeader = document.getElementById('validation-report-header');
    const constraintName = document.getElementById('constraint-name');
    const constraintContext = document.getElementById('constraint-context');
    const constraintResult = document.getElementById('constraint-result');
    const constraintViolationMessageRow = document.getElementById('constraint-violation-message-row');
    const constraintViolationMessage = document.getElementById('constraint-violation-message');

    const name = validationReport.name;
    const context = validationReport.elementType;
    const result = validationReport.result;
    const isConstraintValid = validationReport.valid;
    const violationMessage = validationReport.violationMessage;

    constraintName.innerHTML = name;
    constraintContext.innerHTML = context;
    constraintResult.innerHTML = result;

    if (isConstraintValid) {
        validationReportHeader.classList.add('valid-constraint-validation');
        constraintResult.classList.add('valid-constraint-validation-result');
    } else {
        validationReportHeader.classList.add('invalid-constraint-validation');
        constraintViolationMessageRow.classList.remove('hidden-violation-message')
        constraintResult.classList.add('invalid-constraint-validation-result');
        constraintViolationMessage.innerHTML = violationMessage;
    }
}

function renderInstanceDetails(instance) {
    const modalBody = document.getElementById('modal-body');
    const validationReportElement = document.getElementById('instance-details-template').cloneNode(true);
    modalBody.appendChild(validationReportElement.content);
    const textarea = document.getElementById('instance-details-textarea');
    textarea.value = JSON.stringify(instance, undefined, 2);
}

function renderConstraintDetails(instanceId, constraint) {
    const modalBody = document.getElementById('modal-body');
    const constraintDetails = document.getElementById('constraint-details-template').cloneNode(true);
    modalBody.appendChild(constraintDetails.content);
    document.getElementById('instance-id').value = instanceId;
    const textarea = document.getElementById('constraint-details-textarea');
    textarea.value = JSON.stringify(constraint, undefined, 2);
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

function spawnModal() {
    return new HystModal({
        linkAttributeName: "data-hystmodal",
        afterClose: () => {
            clearModalContent();
        }
    });
}

function clearModalContent() {
    let modalContent = document.getElementById('modal-body');
    while (modalContent.firstChild) {
        modalContent.removeChild(modalContent.lastChild);
    }
}

function closeModal() {
    modalView.close();
}

function openModal() {
    modalView.open();
}