$(function () {
$("#statusId").change(function () {
if ($(this).val() == "6") {
$("#complete").show();
} else {
$("#complete").hide();
}
});
});