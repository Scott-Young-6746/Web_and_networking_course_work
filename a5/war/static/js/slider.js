var slider_mod = (function() {

function Slider( id, handler ) {
    this.id = id;
    this.slider = document.getElementById( id );
    this.div = this.slider.getElementsByTagName('div')[0];
    this.slider.addEventListener('click', this.mkClickHandler(), false );
    this.handler = handler;
}

Slider.prototype = {
    mkClickHandler : function() {
        var slider = this;
        return function( evt ) {
            return slider.click( evt );
        }
    },
    click : function( evt ) {
        evt.stopPropagation();
        evt.preventDefault();
        var off = this.slider.offsetLeft;
        // 10 is twice frame's border and padding, need to calculate
        var size = this.slider.offsetParent.offsetWidth - (2*off + 10);
        var left = off;
        var op = this.slider.offsetParent;
        while( op != null ) {
            left += op.offsetLeft;
            op = op.offsetParent;
        }
        var dx = (evt.clientX - left) -  off;
        if ( dx < 0 ) dx = 0;
        if ( dx > size ) dx = size;
        this.div.style.width =  dx + "px";
        this.handler( dx / size );
    }
}

function initBehaviour( evt ) {
    var demo = document.getElementById('demo');
    new Slider('pl', function(x) { demo.style.paddingLeft = (10*x) + "em"});
    new Slider('pt', function(x) { demo.style.paddingTop = (10*x) + "em"});
    new Slider('pb', function(x) { demo.style.paddingBottom = (10*x) + "em"});
    new Slider('pr', function(x) { demo.style.paddingRight = (10*x) + "em"});

    new Slider('bl', function(x) { demo.style.borderLeftWidth = (30*x)+"px"});
    new Slider('bt', function(x) { demo.style.borderTopWidth = (30*x)+"px"});
    new Slider('bb', function(x) { demo.style.borderBottomWidth =(30*x)+"px"});
    new Slider('br', function(x) { demo.style.borderRightWidth = (30*x)+"px"});

    new Slider('ml', function(x) { demo.style.marginLeft = (50*x) + "%"});
    new Slider('mt', function(x) { demo.style.marginTop = (50*x) + "%"});
    new Slider('mb', function(x) { demo.style.marginBottom = (50*x) + "%"});
    new Slider('mr', function(x) { demo.style.marginRight = (50*x) + "%"});

    new Slider('hs', function(x) { demo.style.height = (50*x) + "%"});
    new Slider('ws', function(x) { demo.style.width = (100*x) + "%"});
}

window.addEventListener('load', initBehaviour, false );

// exported
return {
   Slider: Slider
};
}());
