function showBar(e, str) {
    e.popover({content: str});
    e.popover('show');
    e.on('hidden.bs.popover', function () {
        e.popover('destroy');
    });
}

function checkEmpty(e) {
    var ret = true;
    e.forEach(function (item) {
        if (item.val() == "") {
            ret = false;
            showBar(item, "This field couldn't be empty.");
        }
    });
    return ret;
}
function opChange(s, f) {
    var e = f.firstElementChild.nextElementSibling;
    f.className = "input-group";
    e.readOnly = true;
    switch (s.value) {
        case "int":
            e.value = 4;
            break;
        case "float":
            e.value = 4;
            break;
        case "char":
            e.value = 1;
            break;
        case "string":
            f.className = "input-group has-success";
            e.value = 1;
            e.autofocus = true;
            e.readOnly = false;
            break;
    }
}

function append(selector) {
    count++;
    var p = $(selector).find("div:first").clone(true);
    p.appendTo("#fileds");
//            length;
    var f = p.find("div:last");
    var e = f.find("input:first");
    f.attr("class", "input-group");
    e.attr("readOnly", true);
    e.attr("name", "length" + count);
    e.val(4);
//            name
    f = p.find("div:first");
    e = f.find("input:first");
    e.attr("name", "name" + count);
    e.val("");
//            type
    f = p.find("select");
    f.attr("name", "type" + count);
    f.val("int");
}