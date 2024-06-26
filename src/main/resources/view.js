// Add style to sections (using Bootstrap)
$(function() {
    // TODO: code here to apply layout
});

// Add actions to the Session section
$(function() {

    // Insert CodeMirror DOM inside an element.
    // The CodeMirror instance is attached to the element.
    function addCodeMirror(ele) {
        var cm = CodeMirror(ele, {
            mode : "text/x-sql",
            value: "\n",
            tabSize : 2,
            indentWithTabs : false,
            autofocus: true,
            //line-number causes issues with focus, don't know how to fix yet
            //lineNumbers : true,
        });

        //for autofocus: refresh to adjust the editor, focus to get cursor focus
        cm.on('focus', function(cm, ev) {cm.refresh(); cm.focus();});

        ele.cm = cm;
    }

	function panels() {
		return $("[name='panel']");
	}

    // Make a prototype, initialize it then copy it on demand.
    // Need to do this since CodeMirror for some reason does
    // not work properly if the editor is inserted dynamically.
	var $panelproto = $("#panel-prototype");

	function insertCodeMirrorAfter(prev) {
		//make new panel by cloning the prototype
		var cloned = $panelproto.clone(true);
		var clonedInput = $("[name='input']", cloned).get(0);
		cloned.removeAttr("id");
		cloned.removeClass("prototype");
		//insert code editor
		addCodeMirror(clonedInput);
		prev.after(cloned);
	}

	function runPanel(panel) {
        //post sql from input and update output with response
        var input = $("[name='input']", panel).get(0);
        var text = input.cm.doc.getValue();
        var output = $("[name='output']", panel);
        $.post("app/run", {
            sql : text
        }, function(data, status) {
            output.html(data);
        });
	}

	// create the first panel
	insertCodeMirrorAfter($panelproto);

	// onload: define handlers
	panels().click(function(e) {
		var action = $(e.target).attr("name");
		if (action == "run") {
            runPanel(this);
		} 
		else if (action == "add") {
			insertCodeMirrorAfter($(this));
		} 
		else if (action == "delete") {
			// delete this panel only if there are panels other than this one 
			// and the prototype (which makes at least 3 panels)
			if (panels().length >= 3) {
				$(this).remove();
			}
		}
	}).keydown(function(e) {
	    // run the sql if Ctrl-Enter is pressed
        var isCtrlEnterPressed = (e.ctrlKey || e.metaKey) && (e.keyCode == 13 || e.keyCode == 10); 
        if (isCtrlEnterPressed) {
            runPanel(this);
        }
	});

});