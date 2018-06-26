$(function () {
$("#statusId").change(function () {
if ($(this).val() == "3") {
$("#complete").show();
} else {
$("#complete").hide();
}
});
});