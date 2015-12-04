var network;
var allNodes;

var nodesDataset = new vis.DataSet(nodes);
var edgesDataset = new vis.DataSet(edges);
function redrawALL(){
    var container = document.getElementById('mynetwork');
    var options = {
        autoResize: true,
        height: '100%',
        width: '100%',
        nodes: {
            shape: "dot",
            fixed: true,
            scaling: {
                customScalingFunction: function (min, max, total, value) {
                    return value / total;
                },
                min: 5,
                max: 150
            },
            font: {
                size: 14,
                face: "Tahoma",
                color: "#FFFFFF"
            }
        },
        edges: {
            width: 0.15,
            color: {inherit: 'from'},
            smooth: {
                type: 'continuous'
            }
        },
        physics: false,
        interaction: {
            tooltipDelay: 200,
            hideEdgesOnDrag: true,
            multiselect: true
        },
        layout: {
            hierarchical: {
                direction: 'LR'
            }
        }
    };

    var data = {nodes:nodesDataset, edges:edgesDataset}
    network = new vis.Network(container, data, options);

    allNodes = nodesDataset.get({returnType:"Object"});
}
redrawALL();

function fullscreen(){
    var docElm = document.documentElement;
    if (docElm.requestFullscreen) {
        docElm.requestFullscreen();
    }
    else if (docElm.mozRequestFullScreen) {
        docElm.mozRequestFullScreen();
    }
    else if (docElm.webkitRequestFullScreen) {
        docElm.webkitRequestFullScreen();
    }
    else if (docElm.msRequestFullscreen) {
        docElm.msRequestFullscreen();
    }
}

/*function capture_value(){
alert(123);
}*/

/* document.getElementById("yes").addEventListener("click", function(){
alert(1234);
});*/
/* var radio_yes = document.getElementById('yes');
radio_yes.onclick = handler;

function handler() {
alert('clicked');
}*/
