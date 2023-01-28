<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
    <link rel="stylesheet" href="css/common.css">
    <link rel="stylesheet" href="css/constraint_model_view.css">
    <link rel="stylesheet" href="css/notifications.css">
    <script src="js/constrain_model.js"></script>
    <script src="js/notifications.js"></script>
</head>
<body>
<header class="constraint-space-header">
    <div>
        <div style="margin: 0">Constraint definition space</div>
    </div>
    <div>
        <form action="/" method="get">
            <input type="submit" value="Back" class="back-button">
        </form>
    </div>
</header>
<main>
    <div class="workspace">
        <div class="outer-container">
            <div class="container">
                <div class="inner-container">
                    <div class="model-element-name">ID: ${modelElement.uuid}</div>
                    <div class="model-element-name">ELEMENT: ${modelElement.name}</div>
                    <div class="attributes">
                        <div class="table-name">Attributes</div>
                        <table class="styled-table">
                            <thead>
                            <tr>
                                <th>Key</th>
                                <th>Type</th>
                                <th>Path</th>
                            </tr>
                            </thead>
                            <tbody id="attribute-body">
                            <#list modelElement.attributes as attribute>
                                <tr>
                                    <td>
                                        <div class="restricted-width">
                                            ${attribute.key}
                                        </div>
                                    </td>
                                    <td>
                                        <div class="restricted-width">
                                            ${attribute.datatype}
                                        </div>
                                    </td>
                                    <td>
                                        <div class="restricted-width">
                                            <input class="copy-button" type="button" value="Copy" onclick="copyPath('${attribute.path}')">
                                        </div>
                                    </td>
                                </tr>
                            </#list>
                            </tbody>
                        </table>
                    </div>
                    <div>
                        <div class="table-name">Associations</div>
                        <table class="styled-table">
                            <thead>
                            <tr>
                                <th>Source</th>
                                <th>By relation</th>
                                <th>Target</th>
                                <th>Path</th>
                            </tr>
                            </thead>
                            <tbody id="association-body">
                            <#list modelElement.associations as association>
                                <tr>
                                    <td>
                                        <div class="restricted-width">
                                            #
                                        </div>
                                    </td>
                                    <td>
                                        <div class="restricted-width">
                                            ${association.name}
                                        </div>
                                    </td>
                                    <td>
                                        <div class="restricted-width">
                                            <input class="target-type-button target-type-button-hover"
                                                   type="button"
                                                   title="${association.targetModelElementName}"
                                                   value="${association.targetModelElementName}"
                                                   id="${association.path}"
                                                   onclick="addToOpenedModelElement('${modelElement.name}', '${association.targetModelElementName}', '${association.path}')">
                                        </div>
                                    </td>
                                    <td>
                                        <div class="restricted-width">
                                            <input class="copy-button" type="button" value="Copy" onclick="copyPath('${association.path}')">
                                        </div>
                                    </td>
                                </tr>
                            </#list>
                            </tbody>
                        </table>
                    </div>
                    <div>
                        <input type="button" value="Reset" onclick="location.reload()" class="form-submit-button reset">
                    </div>
                </div>

                <hr>

                <div class="constraints">
                    <div>
                        <div class="constraint-block-title">Constraint functions</div>
                        <div class="functions">
                            <#list functions as function>
                                <input type="hidden" id=${function.functionName} value='${function.template}'>
                                <div onclick="addConstraint('${function.functionName}')">${function.functionName}()</div>
                            </#list>
                        </div>
                    </div>
                </div>

            </div>
        </div>
        <div class="editor">
            <div class="constraint-title">Constraint definition</div>
            <textarea id="constrain_space" cols=50 rows=10 class="constrain_space">${constraintTemplate.template}</textarea>
            <div class="buttons">
                <input type="button" onclick="prettyPrint()" value="Pretty Print" class="form-submit-button">
                <input type="button" onclick="saveConstraint()" value="Save" class="form-submit-button">
            </div>
        </div>
    </div>
</main>
</body>
</html>