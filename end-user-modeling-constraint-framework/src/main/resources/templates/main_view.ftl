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
    <script src="js/main_view.js"></script>
</head>
<body>
<header>
    <div>User-Driven Constraint Modeling for Entity Models at Runtime</div>
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
                        <th>Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list models as model>
                        <tr>
                            <td>${model.name}</td>
                            <td>
                                <form action="/constrain_model?test=#" method="get">
                                    <input type="hidden" name="id" value="${model.uuid}">
                                    <input type="hidden" name="name" value="${model.name}">
                                    <input type="submit" value="Constrain model" class="form-submit-button">
                                </form>
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
                        <th>Details</th>
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
                                    <input class="button-details" type="button"
                                           value="Details"
                                           onclick="displayInstanceDetails('${instance.uuid}')">
                                </td>
                                <#if constraintsByType?size gt 0>
                                    <#if constraintsByType[type]??>
                                        <td>
                                            <#list constraintsByType[type] as constraint>
                                                <div class="display-constraint-window" onclick="displayConstraintById('${constraint.uuid}')">${constraint?counter}) ${constraint.name}</div>
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

<div class="instance-details" id="instance-details">
    <div class="title-instance-details">
        <div style="margin: 1em 2em; color: white; font-weight: bold;">Instance details</div>
        <div class="close" onclick="closeDetailsPopup()">&times;</div>
    </div>

    <textarea id="instance-details-textarea" name="instance_details" cols="70" rows="40" readonly style="resize: none"></textarea>
</div>

<div class="constraint-details" id="constraint-details">
    <div class="title-constraint-detils">
        <div style="margin: 1em 2em; color: white; font-weight: bold;">Constraint details</div>
        <div class="close" onclick="closeDetailsPopup()">&times;</div>
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
</html>

