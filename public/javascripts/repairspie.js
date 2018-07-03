var labels=document.getElementById("chartData").getAttribute("data-labels").split(",");
var data=document.getElementById("chartData").getAttribute("data-data").split(",");
var ctx = document.getElementById("repairsComplete").getContext('2d');
var myChart = new Chart(ctx, {
   type: 'pie',
   data: {
       labels: labels,
       datasets: [{
           label: 'Repairs Completed',
           data: data,
           borderColor: 'rgba(0,0,0,1)',
           backgroundColor: [
                'rgba(0, 255, 0, 1)',
                'rgba(255, 0, 0, 1)',    ],
           borderWidth: 2,
       }]
   },
   options: {
   responsive: false,
       scales: {
       }
   }
});