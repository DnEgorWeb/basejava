<%@ page import="com.urise.webapp.model.ContactType" %>
<%@ page import="com.urise.webapp.model.SectionType" %>
<%@ page import="com.urise.webapp.model.Resume" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/styles.css">
    <title>New Resume</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form>
        <dl>
            <dt>Name:</dt>
            <dd>
                <input
                    type="text"
                    name="fullName"
                    size=50
                    value="${resume.getFullName()}"
                />
            </dd>
            <c:if test="${errors.getFullName() != null}">
                <span class="error">${errors.getFullName()}</span>
            </c:if>
        </dl>
        <h3>Contacts:</h3>
        <c:forEach var="contactType" items="${resume.getContacts().keySet()}">
            <dl>
                <dt>${contactType.getTitle()}</dt>
                <dd><input type="text" name="${contactType.name()}" size=30 value="${resume.getContact(contactType)}"></dd>
                <c:if test="${errors.getContact(contactType) != null}">
                    <span class="error">${errors.getContact(contactType)}</span>
                </c:if>
            </dl>
        </c:forEach>
        <h3 id="sections">Sections:</h3>
        <c:forEach var="sectionType" items="${resume.getStringSections().keySet()}">
            <dl>
                <dt>${sectionType.title}</dt>
                <dd>
                    <input type="text" name="${sectionType.name()}" size=30 value="${resume.getStringSection(sectionType)}" />
                </dd>
                <c:if test="${errors.getStringSection(sectionType) != null}">
                    <span class="error">${errors.getStringSection(sectionType)}</span>
                </c:if>
            </dl>
        </c:forEach>
        <c:forEach var="sectionType" items="${resume.getListSections().keySet()}">
            <dl>
                <dt>${sectionType.title}</dt>
                <dd>
                    <textarea
                        type="text"
                        name="${sectionType.name()}"
                        size=50
                        value="${resume.getListSection(sectionType).get(0)}">
                    </textarea>
                </dd>
                <c:if
                    test="${errors.getListSection(sectionType).get(listItem.index) != null}"
                >
                    <span class="error">${errors.getListSection(sectionType).get(listItem.index)}</span>
                </c:if>
            </dl>
        </c:forEach>
        <c:forEach var="sectionType" items="${resume.companySections.keySet()}">
            <c:forEach var="company" items="${resume.companySections.get(sectionType)}" varStatus="companyIteration">
                <dl name="${sectionType.name()}_0">
                    <fieldset name="group-${sectionType}" style="border: none; margin: 0; padding: 0;">
                        <dt>${sectionType.title}</dt>
                        <dd>
                            <label>название</label>
                            <input
                                type="text"
                                name="name"
                                placeholder=""
                                size=30
                                value="${company.getName()}"
                            />
                            <c:if test="${errors.companySections.get(sectionType)[companyIteration.index].name != null}">
                                <span class="error">
                                    ${errors.companySections.get(sectionType)[companyIteration.index].name}
                                </span>
                            </c:if>
                            <br />
                            <br />
                            <label>сайт</label>
                            <input
                                type="text"
                                name="website"
                                size=30
                                placeholder="https://google.com"
                                value="${company.website}"
                            />
                            <c:if test="${errors.companySections.get(sectionType)[companyIteration.index].website != null}">
                                <span class="error">
                                    ${errors.companySections.get(sectionType)[companyIteration.index].website}
                                </span>
                            </c:if>
                            <c:forEach var="period" items="${company.getPeriods()}" varStatus="periodIteration">
                                <fieldset name="period" style="border: none; margin: 0; padding: 0;">
                                    <br />
                                    <br />
                                    <label>дата начала</label>
                                    <input type="date" name="startDate" value="${period.getStartDate()}" />
                                    <c:if test="${errors.companySections.get(sectionType)[companyIteration.index].periods[periodIteration.index].startDate != null}">
                                        <span class="error">
                                            ${errors.companySections.get(sectionType)[companyIteration.index].periods[periodIteration.index].startDate}
                                        </span>
                                    </c:if>
                                    <br />
                                    <br />
                                    <label>дата окончания</label>
                                    <input type="date" name="endDate" value="${period.getEndDate()}" />
                                    <c:if
                                        test="${errors.companySections.get(sectionType)[companyIteration.index].periods[periodIteration.index].endDate != null}">
                                        <span class="error">
                                            ${errors.companySections.get(sectionType)[companyIteration.index].periods[periodIteration.index].endDate}
                                        </span>
                                    </c:if>
                                    <br />
                                    <br />
                                    <label>название периода</label>
                                    <input type="text" name="title" size=30 placeholder="название" value="${period.getTitle()}" />
                                    <c:if
                                        test="${errors.companySections.get(sectionType)[companyIteration.index].periods[periodIteration.index].title != null}">
                                        <span class="error">
                                            ${errors.companySections.get(sectionType)[companyIteration.index].periods[periodIteration.index].title}
                                        </span>
                                    </c:if>
                                    <br />
                                    <br />
                                    <label>описание периода</label>
                                    <input type="text" name="description" size=30 placeholder="описание" value="${period.getDescription()}" />
                                    <c:if
                                        test="${errors.companySections.get(sectionType)[companyIteration.index].periods[periodIteration.index].description != null}">
                                        <span class="error">
                                            ${errors.companySections.get(sectionType)[companyIteration.index].periods[periodIteration.index].description}
                                        </span>
                                    </c:if>
                                    <br />
                                    <br />
                                    <button type="button" onclick="copyPeriod.apply(this, ['period'])">
                                        Add period
                                    </button>
                                    <button type="button" onclick="removeElement.apply(this, ['period'])">
                                        Remove period
                                    </button>
                                </fieldset>
                                <br />
                                <br />
                            </c:forEach>
                            <button type="button" onclick="copySection.apply(this, ['${sectionType}'])">
                                Add ${sectionType.title.toLowerCase()}
                            </button>
                            <button type="button" onclick="removeElement.apply(this, ['${sectionType}'])">
                                Remove ${sectionType.title.toLowerCase()}
                            </button>
                        </dd>
                    </fieldset>
                </dl>
            </c:forEach>
        </c:forEach>
        <hr>
        <button type="button" onClick="submitForm()">Save</button>
        <button type="button" onclick="window.history.back()">Cancel</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
<script>
    // makes copies of elements that can be duplicated my user
    const experienceSection = document.querySelector('[name=${SectionType.EXPERIENCE.name()}_0]').cloneNode(true);
    const educationSection = document.querySelector('[name=${SectionType.EDUCATION.name()}_0]').cloneNode(true);
    const period = document.querySelector('[name=period]').cloneNode(true);
    // clear all inputs in case there were predefined values
    clearInputs(experienceSection);
    clearInputs(educationSection);
    clearInputs(period);
    clearErrors(experienceSection);
    clearErrors(educationSection);
    clearErrors(period);

    function copySection(name) {
      const section = name === 'EXPERIENCE' ? experienceSection : educationSection
      const elements = document.querySelectorAll('dl[name^=' + name + ']');
      const lastElement = elements[elements.length - 1];
      const newElement = section.cloneNode(true);
      const oldElement = this.closest('[name^=' + name + ']');
      oldElement.parentNode.insertBefore(newElement, oldElement.nextSibling);

      newElement.setAttribute('name', incrementNameIndex(lastElement.getAttribute('name')));
      setInputsNameToValue(newElement);
    }

    function copyPeriod(name) {
        const { newElement } = copyElement.apply(this, [name, period]);
        newElement.setAttribute('name', name);
    }

    function copyElement(name, el) {
        const oldElement = this.closest('[name^=' + name + ']');
        const newElement = el.cloneNode(true);
        oldElement.parentNode.insertBefore(newElement, oldElement.nextSibling);
        return { oldElement, newElement };
    }

    function removeElement(name) {
      const element = this.closest('[name^=' + name + ']');
      if (element) {
        element.remove();
      }
    }

    function setInputsNameToValue(parentElement) {
        const name = parentElement.getAttribute('name');
        var lastUnderscore = name.lastIndexOf("_");
        var numberString = name.substring(lastUnderscore + 1);
        var index = parseInt(numberString);

        let inputs = parentElement.getElementsByTagName('input');
        for (let i = 0; i < inputs.length; i++) {
            inputs[i].name = incrementNameIndex(inputs[i].name, index);
        }
    }


    function incrementNameIndex(str, useIndex) {
        let underscoreIndex = str.lastIndexOf('_');
        if (underscoreIndex !== -1 && !isNaN(parseInt(str.charAt(underscoreIndex + 1)))) {
            let incrementedNumber = useIndex ?? (parseInt(str.substring(underscoreIndex + 1)) + 1);
            return str.substring(0, underscoreIndex + 1) + incrementedNumber;
        } else {
            return str;
        }
    }

    function clearInputs(element) {
        element.querySelectorAll('input, textarea').forEach(input => { input.value = ''; });
    }
    
    function clearErrors(element) {
        const elements = element.getElementsByClassName('error');
        while (elements.length > 0) {
            elements[0].parentNode.removeChild(elements[0]);
        }
    }

    function submitForm() {
        const elements = document.forms[0].elements;

        const payload = {};

        payload.fullName = elements.fullName.value;
        payload.contacts = {
            PHONE: elements.PHONE.value,
            SKYPE: elements.SKYPE.value,
            EMAIL: elements.EMAIL.value,
            LINKEDIN: elements.LINKEDIN.value,
            GITHUB: elements.GITHUB.value,
            STACKOVERFLOW: elements.STACKOVERFLOW.value,
            HOMEPAGE: elements.HOMEPAGE.value,
        };
        payload.stringSections = { OBJECTIVE: elements.OBJECTIVE.value, PERSONAL: elements.PERSONAL.value };
        payload.listSections = {
            ACHIEVEMENT: elements.ACHIEVEMENT.value.split('\n'),
            QUALIFICATIONS: elements.QUALIFICATIONS.value.split('\n'),
        };

        payload.companySections = { EXPERIENCE: getCompanyForm('group-EXPERIENCE'), EDUCATION: getCompanyForm('group-EDUCATION') };

        const form = document.createElement('form');
        form.setAttribute('method', 'post');
        form.setAttribute('action', 'resume');
        form.setAttribute('enctype', 'application/x-www-form-urlencoded');

        var input = document.createElement("input");
        input.setAttribute('type', 'text');
        input.setAttribute('name', 'resume');
        input.setAttribute('type', 'hidden');
        input.value = JSON.stringify(payload);
        console.log('payload.contacts: ', payload.contacts);
        document.body.appendChild(form);
        form.appendChild(input);
        form.submit();
    }

    function getCompanyForm(element) {
        let sections = document.forms[0].elements[element];
        sections = sections.length ? Array.from(sections) : [sections];
        return sections.map(s => {
            const periodSections = [...s.querySelectorAll("[name=period]")];
            const { elements } = s;
            return {
                name: elements.name.value,
                website: elements.website.value,
                periods: periodSections.map(period => {
                    return {
                        title: period.elements.title.value,
                        description: period.elements.description.value,
                        startDate: period.elements.startDate.value,
                        endDate: period.elements.endDate.value,
                    }
                }),
            };
        })
    }
</script>
</body>
</html>
