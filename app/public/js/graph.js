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
            mass: 20,
            scaling: {
                customScalingFunction: function (min, max, total, value) {
                    return value / total;
                },
                min: 10,
                max: 150
            },
            font: {
                size: 14,
                face: "Tahoma",
                color: "0x1b2024"
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
                direction: 'LR',
                levelSeparation: '350',
                sortMethod: 'directed'
            }
        }
    };

    var data;
    network = new vis.Network(container, data, options);
    generateGraph([]);

    // Update course paths when nodes are selected
    network.on("selectNode", function (params) {
        generateGraph(params.nodes);
    });

    // Deselect all nodes in a higher hierarchy than the deselected node
    network.on("deselectNode", function (params) {
        // Figure out distance (hierarchy) of deselected node
        var deselected = arrayDiff(params.previousSelection.nodes, params.nodes);
        var distObj = network.getPositions(deselected);
        var maxDist = distObj[Object.keys(distObj)[0]].x;

        // Keep selecting all nodes with lower or equal distance (hierarchy)
        // than deselected node
        var positionsOfSelected = network.getPositions(network.getSelectedNodes());
        var keepSelected = [];
        Object.keys(positionsOfSelected).forEach(function(node) {
            if (positionsOfSelected[node].x <= maxDist) {
                keepSelected.push(node);
            }
        });
        network.unselectAll();
        network.selectNodes(keepSelected);
    });

    allNodes = nodesDataset.get({returnType:"Object"});
}
redrawALL();

// Generates the graph given the currently selected nodes
function generateGraph(selectNodes) {
    var hier = getHierarchies(selectNodes);
    $.ajax({
        url:'http://localhost:8000/explore',
        type:"POST",
        data: JSON.stringify(hier),
        contentType:"application/json; charset=utf-8",
        dataType:"json",
        success: function (data) {
            if(data) {
                console.log(data);
                network.setData({nodes: new vis.DataSet(data.nodes), edges: new vis.DataSet(data.edges)})
                network.selectNodes(selectNodes);
            }
        },
        error: function(one, status, error) {
            console.log(status);
            console.log(error);
        }
    });
}

// Returns the set difference of two arrays.
// i.e. arrayDiff([1, 2, 3], [1, 2]) => [2]
function arrayDiff(a, b) {
    return a.filter(function(i) {return b.indexOf(i) < 0;});
}

// Given an array of node ids, returns a nested array where each outer
// array represents the hierarhical positions of the nodes in the DAG
function getHierarchies(nodes) {
    var locations = network.getPositions(nodes);
    var distMap = {};
    Object.keys(locations).forEach(function(id) {
        var dist = locations[id].x;
        if (!distMap[dist]) {
            distMap[dist] = [];
        }
        // Strip pipe from ids
        if (id.indexOf('|') >= 0) {
            id = id.split('|')[1];
        }
        distMap[dist].push(id);
    });
    return getValsSortedByKeys(distMap);
}

// Returns an array of the values of this object, sorted
// by the keys of this object
function getValsSortedByKeys(obj) {
    var keys = Object.keys(obj);
    keys.sort();
    var sorted = [];
    for (var i = 0; i < keys.length; ++i) {
        sorted.push(obj[keys[i]]);
    }
    return sorted;
}

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
