// Add style to sections (using Bootstrap)
$(function() {
    function applyStyle($section) {
        // wrap each exercise to make a column
        $section.find('h3').each((idx, exer) => {
            $(exer).nextUntil('h3').addBack()
                .wrapAll('<div class="col-lg-4 col-md-6"></div>');
        });
    
        // reset columns so that columns that does not fit on current line
        // would be properly moved to next line 
        $section.children('div').each((idx, col) => {
            if (idx === 0) return;
            let $col = $(col);
            if (idx % 3 === 0) {
                // reset columns for lg size
                $col.before('<div class="clearfix visible-lg-block"></div>');
            }
            if (idx % 2 === 0) {
                // reset columns for md size
                $col.before('<div class="clearfix visible-md-block"></div>');
            }
        });

        // make a single row
        $section.wrapInner('<div class="row"></div>');
    }

    applyStyle($('#exercises'));
    applyStyle($('#examples'));
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
        $.post("main/run", {
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