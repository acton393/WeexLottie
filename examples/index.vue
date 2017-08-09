<template>
  <scroller>
	<text style='margin-left:350px;margin-bottom:20px;border-width:2px;width:80px;alignItem:center' @click='click' :value="status"></text>
    <div style="flex-direction:row">
      <lottie src='http://127.0.0.1:12580/examples/animations/LottieWalkthrough.json' loop=true style='flex:1;height:300px'  ref='lottie'></lottie>
	    <lottie src='http://127.0.0.1:12580/examples/animations/MotionCorpse-Jrcanest.json' loop=true style='flex:1;height:300px'  ref='lottie1'></lottie>
    </div>
    <div style="flex-direction:row">
      <lottie src='http://127.0.0.1:12580/examples/animations/LineAnimation.json' loop=true style='flex:1;height:300px'  ref='lottie2'></lottie>
	    <lottie src='http://127.0.0.1:12580/examples/animations/TwitterHeart.json' loop=true style='flex:1;height:300px'  ref='lottie3'></lottie>
    </div>
    <div style="flex-direction:row">
      <lottie src='http://127.0.0.1:12580/examples/animations/9squares-AlBoardman.json' loop=true style='flex:1;height:300px'  ref='lottie4'></lottie>
	    <lottie src='http://127.0.0.1:12580/examples/animations/LottieLogo1.json' loop=true style='flex:1;height:300px'  ref='lottie5'></lottie>
    </div>
      <div style="flex-direction:row">
      <lottie src='http://127.0.0.1:12580/examples/animations/HamburgerArrow.json' loop=true style='flex:1;height:300px'  ref='lottie6'></lottie>
	    <lottie src='http://127.0.0.1:12580/examples/animations/Watermelon.json' loop=true style='flex:1;height:300px'  ref='lottie7'></lottie>
    </div>
  </scroller>
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
        var lottie2 = this.$refs.lottie2;
		    var lottie3 = this.$refs.lottie3;
        var lottie4 = this.$refs.lottie4;
		    var lottie5 = this.$refs.lottie5;
        var lottie6 = this.$refs.lottie6;
		    var lottie7 = this.$refs.lottie7;
        if (this.play) {
          this.play = false;
          lottie.reset();
		      lottie1.reset();
          lottie2.reset();
		      lottie3.reset();
          lottie4.reset();
		      lottie5.reset();
          lottie6.reset();
		      lottie7.reset();
		      this.status = "play";
        }else {
          this.play = true;
          lottie.play();
		      lottie1.play();
          lottie2.play();
		      lottie3.play();
          lottie4.play();
		      lottie5.play();
          lottie6.play();
		      lottie7.play();
		      this.status='stop'
        }
      }
    }
  }  
</script>