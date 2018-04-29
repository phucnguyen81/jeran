// Add style to the Exercises section (using Bootstrap)
$(function() {
    var $exercises = $('#exercises');
    
    // wrap each exercise to make a column
    $exercises.find('h3').each((idx, exer) => {
        $(exer).nextUntil('h3').addBack()
            .wrapAll('<div class="col-lg-4 col-md-6"></div>');
    });

    // reset columns so that columns that does not fit on current line
    // would be properly moved to next line 
    $exercises.children('div').each((idx, col) => {
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
    $exercises.wrapInner('<div class="row"></div>');
});

// Add actions to the Session section
$(function() {

	var panels = function() {
		return $("[name='panel']");
	}

	//insert CodeMirror DOM inside an element
	//the CodeMirror instance is attached to the element
	var addCodeMirror = function(ele) {
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

	var panelproto = $("#panel-prototype");
	
	var insertCodeMirrorAfter = function(prev) {
		//make new panel by cloning the prototype
		var cloned = panelproto.clone(true);
		cloned.removeAttr("id");
		cloned.removeClass("prototype");
		//insert code editor
		var clonedInput = $("[name='input']", cloned).get(0);
		addCodeMirror(clonedInput);
		prev.after(cloned);
	}

	//create the first panel
	insertCodeMirrorAfter(panelproto);

	// onload: define handlers
	panels().click(function(e) {
		var panel = $(this);
		var action = $(e.target).attr("name");
		if (action == "submit") {
			//post sql from input and update output with response
			var input = $("[name='input']", panel).get(0);
			var text = input.cm.doc.getValue();
			var output = $("[name='output']", panel);
			$.post("main/submit", {
				sql : text
			}, function(data, status) {
				output.html(data);
			});
		} else if (action == "add") {
			insertCodeMirrorAfter(panel);
		} else if (action == "delete") {
			// delete this panel only if there are panels other than this one 
			// and the prototype
			if (panels().length > 2) {
				panel.remove();
			}
		}
	});

});