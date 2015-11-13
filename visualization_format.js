var nodes = [
  {id: 1,  title: 'Course: ' + 'cosi11:xxxx' + '<br>' + 'cosi21:xxxx' + '<br>' + 'cosi22:xxxx', value: 22, group: 0},
  {id: 2,  title: 'Course: ' + 'cosi11:xxxx' + '<br>' + 'cosi21:xxxx' + '<br>' + 'cosi22:xxxx', value: 24, group: 0},
  {id: 3,  title: 'Course: ' + 'cosi11:xxxx' + '<br>' + 'cosi21:xxxx' + '<br>' + 'cosi22:xxxx', value: 22, group: 0},
  {id: 4,  title: 'Course: ' + 'cosi11:xxxx' + '<br>' + 'cosi21:xxxx' + '<br>' + 'cosi22:xxxx', value: 22, group: 0},
  {id: 5,  title: 'Course: ' + 'cosi11:xxxx' + '<br>' + 'cosi21:xxxx' + '<br>' + 'cosi22:xxxx', value: 26, group: 1},
  {id: 6,  title: 'Course: ' + 'cosi11:xxxx' + '<br>' + 'cosi21:xxxx' + '<br>' + 'cosi22:xxxx', value: 22, group: 1},
  {id: 7,  title: 'Course: ' + 'cosi11:xxxx' + '<br>' + 'cosi21:xxxx' + '<br>' + 'cosi22:xxxx', value: 24, group: 1},
  {id: 8,  title: 'Course: ' + 'cosi11:xxxx' + '<br>' + 'cosi21:xxxx' + '<br>' + 'cosi22:xxxx', value: 34, group: 1},
  {id: 9,  title: 'Course: ' + 'cosi11:xxxx' + '<br>' + 'cosi21:xxxx' + '<br>' + 'cosi22:xxxx', value: 22, group: 2},
  {id: 10, title: 'Course: ' + 'cosi11:xxxx' + '<br>' + 'cosi21:xxxx' + '<br>' + 'cosi22:xxxx', value: 23, group: 2},
  {id: 11, title: 'Course: ' + 'cosi11:xxxx' + '<br>' + 'cosi21:xxxx' + '<br>' + 'cosi22:xxxx', value: 26, group: 2},
  {id: 12, title: 'Course: ' + 'cosi11:xxxx' + '<br>' + 'cosi21:xxxx' + '<br>' + 'cosi22:xxxx', value: 22, group: 2},
  {id: 13, title: 'Course: ' + 'cosi11:xxxx' + '<br>' + 'cosi21:xxxx' + '<br>' + 'cosi22:xxxx', value: 22, group: 3},
  {id: 14, title: 'Course: ' + 'cosi11:xxxx' + '<br>' + 'cosi21:xxxx' + '<br>' + 'cosi22:xxxx', value: 27, group: 3},
  {id: 15, title: 'Course: ' + 'cosi11:xxxx' + '<br>' + 'cosi21:xxxx' + '<br>' + 'cosi22:xxxx', value: 22, group: 3},
  {id: 16, title: 'Course: ' + 'cosi11:xxxx' + '<br>' + 'cosi21:xxxx' + '<br>' + 'cosi22:xxxx', value: 23, group: 3}
];

var edges = [
  {from: 1, to: 15},
  {from: 1, to: 3},
  {from: 1, to: 4},
  {from: 1, to: 5},
  {from: 2, to: 5},
  {from: 2, to: 12},
  {from: 2, to: 3},
  {from: 2, to: 11},
  {from: 2, to: 4},
  {from: 3, to: 5},
  {from: 3, to: 7},
  {from: 3, to: 2},
  {from: 3, to: 10},
  {from: 3, to: 5},
  {from: 4, to: 7},
  {from: 4, to: 8},  
  {from: 4, to: 9},
  {from: 4, to: 10}
];