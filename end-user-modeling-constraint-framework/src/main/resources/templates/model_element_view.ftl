<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Model element view</title>
    <link rel="stylesheet" href="css/common.css">
    <link rel="stylesheet" href="css/model_element_view.css">
    <link rel="stylesheet" href="libs/notification/notifications.css">
    <script src="js/model_element.js"></script>
    <script src="libs/notification/notifications.js"></script>
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
    <div class="workspace" <#if update==true>style="justify-content: start" </#if>>
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
                                <#if update == true>
                                    <th>Action</th>
                                <#else >
                                    <th>Path</th>
                                </#if>

                            </tr>
                            </thead>
                            <tbody id="attribute-body">
                            <#list modelElement.attributes as attribute>
                                <tr>
                                    <td>
                                        <#if update == true>
                                            <input type="text" value="${attribute.key}">
                                        <#else >
                                            <div class="restricted-width">
                                                ${attribute.key}
                                            </div>
                                        </#if>
                                    </td>
                                    <td>
                                        <#if update == true>
                                            <input type="text" value="${attribute.datatype}">
                                        <#else >
                                            <div class="restricted-width">
                                                ${attribute.datatype}
                                            </div>
                                        </#if>
                                    </td>
                                    <td>
                                        <div class="restricted-width">
                                            <#if update == true>
                                                <input class="update-button" type="button" value="Update"
                                                       onclick="updateAttribute(this, '${modelElement.uuid}', '${modelElement.name}', '${attribute.uuid}')">
                                            <#else >
                                                <input class="copy-button" type="button" value="Copy"
                                                       onclick="copyPath('${attribute.path}')">
                                            </#if>
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
                                <#if update == true>
                                    <th>Action</th>
                                <#else >
                                    <th>Path</th>
                                </#if>
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
                                        <#if update == true>
                                            <input type="text" value="${association.name}">
                                        <#else >
                                            <div class="restricted-width">
                                                ${association.name}
                                            </div>
                                        </#if>
                                    </td>
                                    <#if update == true>
                                        <td>
                                            <#if update == true>
                                                <input type="text" value="${association.targetModelElementName}">
                                            <#else >
                                                <div class="restricted-width">
                                                    ${association.targetModelElementName}
                                                </div>
                                            </#if>
                                        </td>
                                        <td>
                                            <input class="update-button" type="button" value="Update"
                                                   onclick="updateAssociation(this, '${modelElement.uuid}', '${modelElement.name}', '${association.uuid}')">
                                        </td>
                                    <#else >
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
                                                <input class="copy-button" type="button" value="Copy"
                                                       onclick="copyPath('${association.path}')">
                                            </div>
                                        </td>
                                    </#if>
                                </tr>
                            </#list>
                            </tbody>
                        </table>
                    </div>
                    <div>
                        <#if update == false>
                            <input type="button" value="Reset" onclick="location.reload()"
                                   class="form-submit-button reset">
                        </#if>
                    </div>
                </div>

                <hr>

                <#if update == false>
                    <div class="constraints">
                        <div>
                            <div class="constraint-block-title">Constraint functions</div>
                            <div class="functions">
                                <#list functions as function>
                                    <input type="hidden" id=${function.functionName} value='${function.template}'>
                                    <div onclick="addConstraint('${function.functionName}')">${function.functionName}()
                                    </div>
                                </#list>
                            </div>
                        </div>
                    </div>
                </#if>

            </div>
        </div>

        <#if update == false>
            <div class="editor">
                <div class="constraint-title">Constraint definition</div>
                <textarea id="constrain_space" cols=50 rows=10
                          class="constrain_space">${constraintTemplate.template}</textarea>
                <div class="buttons">
                    <input type="button" onclick="prettyPrint()" value="Pretty Print" class="form-submit-button">
                    <input type="button" onclick="saveConstraint('${modelElement.name}')" value="Save" class="form-submit-button">
                </div>
            </div>
        </#if>
    </div>
</main>
</body>
</html>