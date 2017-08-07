<template>
  <div style='margin-top:50px;margin-left:150px'>
	<text style='margin-left:150px;border-width:2px;width:80px;' @click='click' :value="status"></text>
    <lottie src='http://127.0.0.1:12580/examples/animations/LottieWalkthrough.json' loop=true style='width:300px;height:300px'  ref='lottie'></lottie>
	<lottie src='http://127.0.0.1:12580/examples/animations/MotionCorpse-Jrcanest.json' loop=true style='width:300px;height:300px'  ref='lottie1'></lottie>
  </div>
</template>

<script>
  module.exports = {
    data: {
      play:false,
	  status:"play"
    },
    methods: {
      click : function(e) {
        var lottie = this.$refs.lottie;
		var lottie1 = this.$refs.lottie1;
        if (this.play) {
          this.play = false;
          lottie.reset();
		  lottie1.reset();
		  this.status = "play";
        }else {
          this.play = true;
          lottie.play();
		  lottie1.play();
		  this.status='stop'
        }
      }
    }
  }  
</script>