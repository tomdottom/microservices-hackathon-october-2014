// Morris.js Charts sample data for SB Admin template

$(function() {

    // Area Chart
    Morris.Area({
        element: 'morris-area-chart',
        data: [
        ],
        xkey: 'period',
        ykeys: ['planets'],
        labels: ['Planets'],
        pointSize: 2,
        hideHover: 'auto',
        resize: true
    });
});
