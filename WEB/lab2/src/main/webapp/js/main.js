const canvas = document.getElementById('canvas');
const canvasController = new CanvasController(canvas);
//const dbManager = new DBManager();

let plane = new Plane(canvasController, 1);
plane.initObjects();
canvasController.updateFrame();