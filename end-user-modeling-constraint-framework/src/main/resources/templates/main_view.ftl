<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Models</title>
    <link rel="stylesheet" href="css/main_view.css">
    <link rel="stylesheet" href="css/common.css">
    <link rel="stylesheet" href="libs/notification/notifications.css">
    <link rel="stylesheet" href="libs/modal/hystmodal.min.css">
    <script src="libs/modal/hystmodal.min.js"></script>
    <script src="js/main_view.js"></script>
    <script src="libs/notification/notifications.js"></script>
</head>
<body>
<header>
    <div>User-Driven Constraint Modeling for Entity Models at Runtime</div>
    <div>Implemented with ${implementation}</div>
</header>
<main>
    <div class="container">
        <div class="inner-container">
            <div class="model-elements">
                <div class="smaller">Model elements</div>
                <table class="styled-table">
                    <thead>
                    <tr>
                        <th>Model</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list models as model>
                        <tr>
                            <td>${model.name}</td>
                            <td>
                                <div class="actions">
                                    <form action="/constrain_model_element?" method="get">
                                        <input type="hidden" name="id" value="${model.uuid}">
                                        <input type="hidden" name="name" value="${model.name}">
                                        <input type="submit" value="constrain" class="form-submit-button">
                                    </form>
                                    <form action="/update_model_element?" method="get">
                                        <input type="hidden" name="id" value="${model.uuid}">
                                        <input type="hidden" name="name" value="${model.name}">
                                        <input type="submit" value="edit" class="form-submit-button">
                                    </form>
                                    <form action="/create_instance?" method="get">
                                        <input type="hidden" name="id" value="${model.uuid}">
                                        <input type="hidden" name="name" value="${model.name}">
                                        <input type="submit" value="instantiate" class="form-submit-button">
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
            <div class="instance-elements">
                <div class="smaller">Instance elements</div>
                <table class="styled-table">
                    <thead>
                    <tr>
                        <th>Id</th>
                        <th>Of type</th>
                        <th>Actions</th>
                        <th>Constraints</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list typeToInstances as type, instances>
                        <#list instances as instance>
                            <tr>
                                <td>${instance.uuid}</td>
                                <td>${type}</td>
                                <td>
                                    <div class="actions instance-elements">
                                        <input class="button-details" type="button"
                                               value="details"
                                               data-hystmodal="#myModal"
                                               onclick="displayInstanceDetails('${instance.uuid}')">
                                        <input class="button-details" type="button"
                                               value="delete"
                                               onclick="deleteInstanceById('${instance.uuid}')">
                                        <form action="/update_instance?" method="get">
                                            <input type="hidden" name="id" value="${instance.uuid}">
                                            <input type="submit" value="update" class="button-details">
                                        </form>
                                    </div>
                                </td>
                                <#if constraintsByType?size gt 0>
                                    <#if constraintsByType[type]??>
                                        <td>
                                            <#list constraintsByType[type] as constraint>
                                                <div class="display-constraint-window"
                                                     data-hystmodal="#myModal"
                                                     onclick="displayConstraintById('${instance.uuid}', '${constraint.uuid}')">${constraint?counter}) ${constraint.name}</div>
                                            </#list>
                                        </td>
                                    <#else>
                                        <td>Empty</td>
                                    </#if>
                                <#else>
                                    <td>Empty</td>
                                </#if>

                            </tr>
                        </#list>
                    </#list>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</main>
</body>

<div class="hystmodal" id="myModal" aria-hidden="true">
    <div class="hystmodal__wrap">
        <div class="hystmodal__window" role="dialog" aria-modal="true">
            <button data-hystclose class="hystmodal__close">Close</button>
            <div id="modal-body">
            </div>
        </div>
    </div>
</div>

<template id="constraint-details-template">
    <div class="constraint-details" id="constraint-details">
        <input type="hidden" id="instance-id">
        <div class="title-constraint-detils">
            <div style="margin: 1em 2em; color: white; font-weight: bold;">Constraint details</div>
        </div>
        <textarea id="constraint-details-textarea" name="constraint_details" cols="70" rows="40" readonly style="resize: none"></textarea>
        <div>
            <button type="button"
                    onclick="validateConstraint()"
                    id="validate-button"
                    class="button-details"
                    style="margin: 1em;">Validate</button>
            <button type="button"
                    onclick="removeConstraint()"
                    id="delete-button"
                    class="button-details"
                    style="margin: 1em;">Remove</button>
        </div>
    </div>
</template>

<template id="instance-details-template">
    <div class="instance-details" id="instance-details">
        <div class="title-instance-details">
            <div style="margin: 1em 2em; color: white; font-weight: bold;">Instance details</div>
        </div>

        <textarea id="instance-details-textarea" name="instance_details" cols="70" rows="40" readonly style="resize: none"></textarea>
    </div>
</template>

<template id="validation-report-template">
    <div id="validation-report">
        <header id="validation-report-header">Constraint validation report</header>
        <table>
            <tr>
                <td class="bold">Constraint name</td>
                <td id="constraint-name"></td>
            </tr>
            <tr>
                <td class="bold">Context</td>
                <td id="constraint-context"></td>
            </tr>
            <tr>
                <td class="bold">Result</td>
                <td id="constraint-result"></td>
            </tr>
            <tr id="constraint-violation-message-row" class="hidden-violation-message">
                <td class="bold">Details</td>
                <td id="constraint-violation-message"></td>
            </tr>
        </table>
    </div>
</template>

</html>

