<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Instance view</title>
    <link rel="stylesheet" href="css/common.css">
    <link rel="stylesheet" href="css/instance_view.css">
    <link rel="stylesheet" href="libs/notification/notifications.css">
    <script src="libs/notification/notifications.js"></script>
    <script src="js/instance_view.js"></script>
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
                    <#if update>
                        <div class="model-element-name">UUID: ${instance.uuid}</div>
                    </#if>
                    <#if !update>
                        <div class="model-element-name">OF TYPE: ${modelElement.name}</div>
                    <#else >
                        <div class="model-element-name">INSTANCE OF: ${instance.instanceOf}</div>
                    </#if>
                    <div class="slots">
                        <div class="table-name">Slots</div>
                        <table class="styled-table">
                            <thead>
                            <tr>
                                <th>Key</th>
                                <th>Value</th>
                            </tr>
                            </thead>
                            <tbody id="slots-body">
                            <#if update == false>
                                <#list modelElement.attributes as attribute>
                                    <tr>
                                        <td>
                                            <div class="restricted-width" attribute-key>${attribute.key}</div>
                                        </td>
                                        <td>
                                            <div class="restricted-width">
                                                <input type="text" id="${attribute.key}" placeholder="Add value" attribute-value>
                                            </div>
                                        </td>
                                    </tr>
                                </#list>
                            <#else>
                                <#list instance.slots as slot>
                                    <tr>
                                        <td>
                                            <div class="restricted-width" slot-key>${slot.key}</div>
                                        </td>
                                        <td>
                                            <div class="restricted-width">
                                                <input type="text" value="${slot.value}" slot-value>
                                            </div>
                                        </td>
                                    </tr>
                                </#list>
                            </#if>
                            </tbody>
                        </table>
                    </div>
                    <div>
                        <div class="table-name">Links</div>
                        <table class="styled-table">
                            <thead>
                            <tr>
                                <th>Relation</th>
                                <th>Target(id)</th>
                                <th></th>
                            </tr>
                            </thead>
                            <tbody id="links-body">
                            <#if update == false>
                                <#list modelElement.associations as association>
                                    <tr>
                                        <td>
                                            <div class="restricted-width" association-name>
                                                ${association.name}
                                            </div>
                                        </td>
                                        <td>
                                            <div class="restricted-width">
                                                <input type="text" placeholder="Enter concrete value" association-value>
                                            </div>
                                        </td>
                                        <td>
                                            <div class="add-button" onclick="newAssociation(this)">&plus;</div>
                                        </td>
                                    </tr>
                                </#list>
                            <#else >
                                <#list instance.links as link>
                                    <tr>
                                        <td>
                                            <div class="restricted-width" link-name>
                                                ${link.name}
                                            </div>
                                        </td>
                                        <td>
                                            <div class="restricted-width">
                                                <input type="text" value="${link.targetInstanceUuid!""}" link-value>
                                            </div>
                                        </td>
                                        <td>
                                            <div class="add-button" onclick="newAssociation(this)">&plus;</div>
                                        </td>
                                    </tr>
                                </#list>
                            </#if>
                            </tbody>
                        </table>
                    </div>
                    <div>
                        <#if update == false>
                            <input type="button" value="instantiate" onclick="createInstance('${modelElement.name}')"
                                   class="form-submit-button action-button">
                        <#else>
                            <input type="button" value="update" onclick="updateInstance('${instance.uuid}')"
                                   class="form-submit-button action-button">
                        </#if>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>
</body>
</html>