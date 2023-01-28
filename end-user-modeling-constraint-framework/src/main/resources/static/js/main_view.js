async function displayInstanceDetails(instanceId) {
    toggleInputs(true);
    const result = await fetch('/get_instance_by_id?' + new URLSearchParams({uuid: instanceId}))

    if (result.ok) {
        const instance = await result.json();
        const textarea = document.getElementById('instance-details-textarea');
        blurMainElement(true);
        toggleInstancePopup(true);
        textarea.value = JSON.stringify(instance, undefined, 2);
    }
}

async function displayConstraintById(constraintId) {
    toggleInputs(true);
    const result = await fetch('/get_constraint_by_id?' + new URLSearchParams({uuid: constraintId}))
    if (result.ok) {
        const constraint = await result.json();
        const textarea = document.getElementById('constraint-details-textarea');
        blurMainElement(true);
        toggleConstraintPopup(true);
        textarea.value = JSON.stringify(constraint, undefined, 2);
    }
}

async function validateConstraint() {
    const textarea = document.getElementById('constraint-details-textarea');
    const constraint = JSON.parse(textarea.value);
    const constraintUuid = constraint.uuid;
    const result = await fetch('validate_constraint_by_id?' + new URLSearchParams({uuid: constraintUuid}));
    if (result.ok) {
        closeDetailsPopup();
    }
}

async function removeConstraint() {
    const textarea = document.getElementById('constraint-details-textarea');
    const constraint = JSON.parse(textarea.value);
    const constraintUuid = constraint.uuid;
    const result = await fetch('/remove_constraint_by_id?' + new URLSearchParams({uuid: constraintUuid}))
    if (result.ok) {
        closeDetailsPopup();
        location.reload();
    }
}

function toggleInputs(disabled) {
    const inputs = document.getElementsByTagName('input');
    for (let index = 0; index < inputs.length; index++) {
        inputs[index].disabled = disabled;
    }
}

function toggleInstancePopup(showWindow) {
    const instanceDetails = document.getElementById('instance-details');
    showWindow ? instanceDetails.style.display = 'block' : instanceDetails.style.display = 'none';
}

function toggleConstraintPopup(showWindow) {
    const constraintDetails = document.getElementById('constraint-details');
    showWindow ? constraintDetails.style.display = 'block' : constraintDetails.style.display = 'none';
}

function blurMainElement(shouldBlur) {
    const main = document.getElementsByTagName('main').item(0);
    shouldBlur ? main.style.filter = 'blur(3px)' : main.style.filter = 'none';
}

function closeDetailsPopup() {
    toggleInputs(false);
    blurMainElement(false);
    toggleInstancePopup(false)
    toggleConstraintPopup(false);
}