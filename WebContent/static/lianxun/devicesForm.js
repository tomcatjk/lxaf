$(document).ready(function () {
    var url = "${ctx}/lu/masters/findMastersListByCid?cid=${devices.customerid}";
    $.post(url,function (mastersList) {
        var options = "";
        $.each(mastersList,function (index,masters) {
            options += "<option value="+masters.mid+">"+masters.name+"</option>"
        })
        $("#masterSelect").html(options);
        var mid = $("#masterSelect").val();
        var url = "${ctx}/lu/defences/findDefencesListByMasterId?masterId=" + mid;
        $.post(url,function (defencesListJson) {
            var options = "";
            $.each(defencesListJson,function (index,defences) {
                options += "<option value="+defences.did+">"+defences.code+"</option>"
            })
            $("#defenceSelect").html(options);
            var did = $("#defenceSelect").val();
            var url = "${ctx}/lu/defences/findDefencesbyDid?did=" + did;
            $.post(url,function (defences) {
                var input = "<input type='text' name='name' value='"+defences.name+"' class='form-control'>";
                $("#defencesName").html(input);
            })
        })
    })

})

$(document).ready(function(){
    $("#masterSelect").change(function(){
        var mid = $(this).val();
        console.log(mid);
        var url = "${ctx}/lu/defences/findDefencesListByMasterId?masterId=" + mid;
        $.post(url,function (defencesListJson) {
            var options = "";
            $.each(defencesListJson,function (index,defences) {
                options += "<option value="+defences.did+">"+defences.code+"</option>"
            })
            $("#defenceSelect").html(options);
            var did = $("#defenceSelect").val();
            var url = "${ctx}/lu/defences/findDefencesbyDid?did=" + did;
            $.post(url,function (defences) {
                var input = "<input type='text' name='defencesName' value='"+defences.name+"' class='form-control'>";
                $("#defencesName").html(input);
            })
        })
    });
});



$(document).ready(function(){
    $("#defenceSelect").change(function(){
        var did = $(this).val();
        var url = "${ctx}/lu/defences/findDefencesbyDid?did=" + did;
        $.post(url,function (defences) {
            var input = "<input type='text' name='defencesName' value='"+defences.name+"' class='form-control'>";
            $("#defencesName").html(input);
        })
    });
});