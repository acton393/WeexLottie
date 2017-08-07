

const WXLottie = {
  show() {
      alert("module WXLottie is created sucessfully ")
  }
};


var meta = {
   WXLottie: [{
    name: 'show',
    args: []
  }]
};



if(window.Vue) {
  weex.registerModule('WXLottie', WXLottie);
}

function init(weex) {
  weex.registerApiModule('WXLottie', WXLottie, meta);
}
module.exports = {
  init:init
};
