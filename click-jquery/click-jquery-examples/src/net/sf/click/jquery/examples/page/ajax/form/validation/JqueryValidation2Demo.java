/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.click.jquery.examples.page.ajax.form.validation;

import java.util.List;
import net.sf.click.jquery.control.ajax.JQForm;
import net.sf.click.jquery.examples.control.JQColorPicker;
import net.sf.click.jquery.examples.control.html.Div;
import net.sf.click.jquery.examples.control.html.Text;
import org.apache.click.Control;
import net.sf.click.jquery.examples.page.BorderPage;
import net.sf.click.jquery.examples.util.JQValidationHelper;
import net.sf.click.jquery.taconite.JQTaconite;
import org.apache.click.Partial;
import org.apache.click.ajax.AjaxBehavior;
import org.apache.click.control.Checkbox;
import org.apache.click.control.FieldSet;
import org.apache.click.control.FileField;
import org.apache.click.control.Option;
import org.apache.click.control.Radio;
import org.apache.click.control.RadioGroup;
import org.apache.click.control.Select;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.apache.click.element.JsImport;
import org.apache.click.element.JsScript;
import org.apache.click.extras.control.CreditCardField;
import org.apache.click.extras.control.DoubleField;
import org.apache.click.extras.control.EmailField;
import org.apache.click.extras.control.IntegerField;
import org.apache.click.extras.control.PickList;
import org.apache.click.extras.control.TelephoneField;

/**
 * This example demonstrates how to validate a Form using using the jQuery
 * Validator plugin: http://docs.jquery.com/Plugins/Validation
 */
public class JqueryValidation2Demo extends BorderPage {

	private static final long serialVersionUID = 1L;

    private JQForm form = new JQForm("form");

    private Div msgHolder = new Div("msgHolder");

    private Text textMsg = new Text();

    @Override
    public void onInit() {
        super.onInit();

        addControl(form);
        addControl(msgHolder);
        msgHolder.add(textMsg);

        JsScript script = new JsScript();
        script.setTemplate("/click-jquery/example/template/jquery.validate2.template.js");
        form.getJQBehavior().setSetupScript(script);

        // Setup fields
        FieldSet fieldSet = new FieldSet("Core Fields");
        form.add(fieldSet);

        // Setup fields
        TextField field = new TextField("firstName", true);
        field.setMinLength(2);
        field.setMaxLength(15);
        fieldSet.add(field);
        field = new TextField("lastName", true);
        field.setMinLength(2);
        field.setMaxLength(15);
        fieldSet.add(field);
        Select select = new Select("select", true);
        select.add("[empty]");
        select.add("one");
        select.add("two");
        fieldSet.add(select);
        fieldSet.add(new FileField("fileField"));
        fieldSet.add(new Checkbox("check", true));
        RadioGroup radioGroup = new RadioGroup("radioGroup");
        radioGroup.add(new Radio("A"));
        radioGroup.add(new Radio("B"));
        radioGroup.add(new Radio("C"));
        fieldSet.add(radioGroup);

        fieldSet = new FieldSet("Extra Fields");
        form.add(fieldSet);

        fieldSet.add(new TelephoneField("telephoneField", true));
        fieldSet.add(new JQColorPicker("colorPicker"));
        IntegerField integerField = new IntegerField("integerField");
        integerField.setRequired(true);
        integerField.setMaxValue(100);
        integerField.setMinValue(5);
        fieldSet.add(integerField);
        DoubleField doubleField = new DoubleField("doubleField");
        doubleField.setMaxValue(100);
        doubleField.setMinValue(5);
        fieldSet.add(doubleField);
        fieldSet.add(new EmailField("email", "E-Mail", true));

        CreditCardField creditCardField = new CreditCardField("credit");
        fieldSet.add(creditCardField);

        PickList pickList = new PickList("languages");
        pickList.setRequired(true);
        pickList.setHeaderLabel("Languages", "Selected");

        pickList.add(new Option("002", "C/C++"));
        pickList.add(new Option("003", "C#"));
        pickList.add(new Option("004", "Fortran"));
        pickList.add(new Option("005", "Java"));
        pickList.add(new Option("006", "Ruby"));
        pickList.add(new Option("007", "Perl"));
        pickList.add(new Option("008", "Visual Basic"));

        pickList.addSelectedValue("004");
        fieldSet.add(pickList);

        Submit submit = new Submit("save");
        form.add(submit);

        // Set AjaxListener on Submit which will be invoked when form is submitted
        submit.addBehavior(new AjaxBehavior() {

            public Partial onAction(Control source) {
                if (form.isValid()) {
                    saveForm();
                    return createSuccessResponse();
                } else {
                    return createErrorResponse();
                }
            }
        });

        JQValidationHelper validator = new JQValidationHelper();
        form.getJQBehavior().getModel().put("messages", validator.getMessages(form));
        form.getJQBehavior().getModel().put("rules", validator.getRules(form));
        form.getJQBehavior().getModel().put("validators", validator.getCustomValidators());
    }

    @Override
    public List getHeadElements() {
        if (headElements == null) {
            headElements = super.getHeadElements();

            // Import JQuery validate plugin
            headElements.add(new JsImport("/click-jquery/example/validate2/jquery.validate.js"));
        }
        return headElements;
    }

    private void saveForm() {
        System.out.println("Form saved to database");
    }

    /**
     * Return a Partial response (using a Taconite Partial object)
     * that does the following:
     *
     * 1. Replace the Form in the browser with the current Form
     * 2. Style the message holder with a green background which indicates success
     * 3. Replace the message holder with the current message holder
     */
    private Partial createSuccessResponse() {
        JQTaconite partial = new JQTaconite();

        // 1. Replace the Form in the browser with the current one
        partial.replace(form);

        // Set a success message
        textMsg.setText("Successfully submitted Form");

        // 2. Style the message holder with a red background
        msgHolder.setAttribute("style", "color:white; background: green; border: black 1px solid;padding: 5px; float: left");

        // 3. Replace the message holder in the browser with the current one
        partial.replace(msgHolder);

        return partial;
    }

    /**
     * Return a Partial response (using a Taconite Partial object)
     * that does the following:
     *
     * 1. Replace the Form in the browser with the current Form
     * 2. Style the message holder with a red background which indicates an error
     * 3. Replace the message holder with the current message holder
     */
    private Partial createErrorResponse() {
        JQTaconite partial = new JQTaconite();

        // 1. Replace the Form in the browser with the current one
        partial.replace(form);

        // Set an error message
        textMsg.setText("Form contained errors.");

        // 2. Style the message holder with a red background
        msgHolder.setAttribute("style", "color:white; background: red; border: black 1px solid;padding: 5px; float: left");

        // 3. Replace the message holder in the browser with the current one
        partial.replace(msgHolder);

        return partial;
    }
}