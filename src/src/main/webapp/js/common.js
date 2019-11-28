(function($) {
	$(document).ready(function(){
		
  $.fn.menu = function(options) {

      var cssmenu = $(this), settings = $.extend({
        title: "Menu",
        format: "dropdown",
        sticky: false
      }, options);

      return this.each(function() {
        cssmenu.prepend('<div id="menu-button">' + settings.title + '</div>');
        $(this).find("#menu-button").on('click', function(){
          $(this).toggleClass('menu-opened');
          var mainmenu = $(this).next('ul');
          if (mainmenu.hasClass('open')) { 
            mainmenu.hide().removeClass('open');
          }
          else {
            mainmenu.show().addClass('open');
            if (settings.format === "dropdown") {
              mainmenu.find('ul').show();
            }
          }
        });

        cssmenu.find('li ul').parent().addClass('has-sub');

        multiTg = function() {
          cssmenu.find(".has-sub").prepend('<span class="submenu-button"></span>');
          cssmenu.find('.submenu-button').on('click', function() {
            $(this).toggleClass('submenu-opened');
            if ($(this).siblings('ul').hasClass('open')) {
              $(this).siblings('ul').removeClass('open').hide();
            }
            else {
              $(this).siblings('ul').addClass('open').show();
            }
          });
        };

        if (settings.format === 'multitoggle') multiTg();
        else cssmenu.addClass('dropdown');

        if (settings.sticky === true) cssmenu.css('position', 'fixed');

        resizeFix = function() {
          if ($( window ).width() > 768) {
            cssmenu.find('ul').show();
          }

          if ($(window).width() <= 768) {
            cssmenu.find('ul').hide().removeClass('open');
          }
        };
        resizeFix();
        return $(window).on('resize', resizeFix);

      });
  };
  if( $('.leftmiddleFrame').is(':empty') )
	// $(".leftmiddleFrame").append("<img id='img' src='../img/office.png' width=1px height=5px/>");
 
  if( $('.rightmiddleFrame').is(':empty') )
	  $(".rightmiddleFrame").append("<img id='img' src='../img/Tammy.JPG'/>");
  
  $("#cssmenu").menu({
	   title: "Menu",
	   format: "multitoggle"
	});
  $(" #cssmenu ul > li#home ").one().click(function(e){
	    e.stopImmediatePropagation();
	    e.preventDefault();
	    $(".rightmiddleFrame").empty();
	    $(".leftmiddleFrame").empty();
	   // $(".leftmiddleFrame").append("<img id='img' src='../img/gray.png'/>");
	    $(".rightmiddleFrame").append("<img id='img' src='../img/Tammy.JPG'/>");
  });
  $(" #cssmenu ul ul ul > li#newDoc ").one().click(function(e){
	    e.stopImmediatePropagation();
	    e.preventDefault();
		
		var menuID = this.id;
		//alert(menuID);
		if(menuID == "newDoc"){
			//alert("in new open");
			sessionStorage.removeItem("diagcounter");
			sessionStorage.removeItem("activitycounter");
			sessionStorage.removeItem("theader");
			sessionStorage.removeItem("tfirstname");
			sessionStorage.removeItem("tlastname");
			sessionStorage.removeItem("tmiddlename");
			sessionStorage.removeItem("tprefix");
			sessionStorage.removeItem("taddress1");
			sessionStorage.removeItem("taddress2");
			sessionStorage.removeItem("tcity");
			sessionStorage.removeItem("tzipcode");
			sessionStorage.removeItem("tstate");
			sessionStorage.removeItem("thomephone");
			sessionStorage.removeItem("tworkphone");
			sessionStorage.removeItem("tmobile");
			sessionStorage.removeItem("tid");
			sessionStorage.removeItem("tein");
			sessionStorage.removeItem("tlicense");
			sessionStorage.removeItem("tflag");		
			sessionStorage.removeItem("tmark");
			sessionStorage.removeItem("tarrtActivityType");
			sessionStorage.removeItem("tarrtRateperVal");
			sessionStorage.removeItem("tarrtRate");
			sessionStorage.removeItem("tarrtRateper");
			$(".rightmiddleFrame").empty();
		   // $(".leftmiddleFrame").empty();	
			 $(".leftmiddleFrame").load("leftside.html", function() {
			        $("#docLeftMenu").show();
			    });
			$(".rightmiddleFrame").show().load("doctor.html"); 
			
		}
	});
  $(" #cssmenu ul ul ul > li#openDoc ").one().click(function(e){
	    e.stopImmediatePropagation();
	    e.preventDefault();
	    sessionStorage.removeItem("diagcounter");
	    sessionStorage.removeItem("activitycounter");
		var menuID = this.id;
		//alert(menuID);
		if(menuID == "openDoc"){
			$(".rightmiddleFrame").empty();
		    $(".leftmiddleFrame").empty();
			 $(".leftmiddleFrame").load("leftside.html", function() {
			        $("#docLeftMenu").show();
			    });
			$(".rightmiddleFrame").show().load("search.html", function() {
		        $("#searchTherapist").show();
		    }); 
			
		}
	});
  $(" #cssmenu ul ul ul > li#newCus").one().click(function(e){
	  	
	  	e.stopImmediatePropagation();
	    e.preventDefault();
		
		var menuID = this.id;
		//alert(menuID);
		
		if(menuID == "newCus"){
			sessionStorage.removeItem("diagcounter");
			sessionStorage.removeItem("activitycounter");
			sessionStorage.removeItem("theader");
			sessionStorage.removeItem("cfirstname");
			sessionStorage.removeItem("clastname");
			sessionStorage.removeItem("cmiddlename");
			sessionStorage.removeItem("cprefix");
			sessionStorage.removeItem("csex");
			sessionStorage.removeItem("caddress1");
			sessionStorage.removeItem("caddress2");
			sessionStorage.removeItem("ccity");
			sessionStorage.removeItem("czip");
			sessionStorage.removeItem("cstate");
			sessionStorage.removeItem("chomephone");
			sessionStorage.removeItem("cworkphone");
			sessionStorage.removeItem("cmobile");
			sessionStorage.removeItem("cactivity");
			sessionStorage.removeItem("cselectFlag");
			sessionStorage.removeItem("ccpt");
			sessionStorage.removeItem("cdob");
			sessionStorage.removeItem("cdov");
			sessionStorage.removeItem("cstheraphy");
			sessionStorage.removeItem("cpayment");
			
			sessionStorage.removeItem("ctherapistid");
			sessionStorage.removeItem("ctherapistName");
			sessionStorage.removeItem("creferral");
			sessionStorage.removeItem("cdiagnosiscd");
			sessionStorage.removeItem("tarrtActivityType");
			sessionStorage.removeItem("tarrtRateperVal");
			sessionStorage.removeItem("tarrtRate");
			sessionStorage.removeItem("tarrtRateper");
			sessionStorage.removeItem("cwaiver");
			sessionStorage.removeItem("cnetch");
			
			$(".rightmiddleFrame").empty();
		    $(".leftmiddleFrame").empty();
		$(".leftmiddleFrame").load("leftside.html", function() {
			        $("#clientLeftMenu").show();
		});
		$(".rightmiddleFrame").show().load("client.html"); 
		}
	});
  $(" #cssmenu ul ul ul > li#openCus ").one().click(function(e){
	    e.stopImmediatePropagation();
	    e.preventDefault();
	    sessionStorage.removeItem("diagcounter");
	    sessionStorage.removeItem("activitycounter");
		var menuID = this.id;
		//alert(menuID);
		if(menuID == "openCus"){
			$(".rightmiddleFrame").empty();
		    $(".leftmiddleFrame").empty();
				
			 $(".leftmiddleFrame").load("leftside.html", function() {
			        $("#clientLeftMenu").show();
			    });
			$(".rightmiddleFrame").show().load("search.html", function() {
		        $("#searchClient").show();
		    }); 
			
		}
	});
  $(" #cssmenu ul ul ul > li#ymreport ").one().click(function(e){
	    e.stopImmediatePropagation();
	    e.preventDefault();
	    sessionStorage.removeItem("diagcounter");
	    sessionStorage.removeItem("activitycounter");
		var menuID = this.id;
		
		if(menuID == "ymreport"){
			
			$(".rightmiddleFrame").empty();
		    $(".leftmiddleFrame").empty();
				
			 $(".leftmiddleFrame").load("leftside.html", function() {
			        $("#reportLeftMenu").show();
			    });
			 $(".rightmiddleFrame").show().load("reportym.html"); 
			
		}
	});
  $(" #cssmenu ul ul ul > li#reportgen").one().click(function(e){
	    e.stopImmediatePropagation();
	    e.preventDefault();
	    
	    sessionStorage.removeItem("diagcounter");
	    sessionStorage.removeItem("activitycounter");
		var menuID = this.id;
	
		if(menuID == "reportgen"){
			
			$(".rightmiddleFrame").empty();
		    $(".leftmiddleFrame").empty();
				
			 $(".leftmiddleFrame").load("leftside.html", function() {
			        $("#reportLeftMenu").show();
			    });
			 $(".rightmiddleFrame").show().load("reportgen.html"); 
			
		}
	});

 /* $("ul#therapist ul").one().on('click', 'li#newDocRate', function(e){
	  
	  	e.stopImmediatePropagation();
	    e.preventDefault();
		var menuID = this.id;
		
		if(menuID == "newDocRate"){
		//$(".leftmiddleFrame").show().load("leftside.html");
		$(".leftmiddleFrame").load("leftside.html", function() {
	        $("#docLeftMenu").show();
	    });
		$(".rightmiddleFrame").show().load("therapistrate.html"); 
		}
	});*/
  /*$("ul#therapist ul").one().on('click', 'li#newClientRate', function(e){
	  
	  	e.stopImmediatePropagation();
	    e.preventDefault();
		var menuID = this.id;
		
		if(menuID == "newClientRate"){
		//$(".leftmiddleFrame").show().load("leftside.html");
			$(".rightmiddleFrame").empty();
		    $(".leftmiddleFrame").empty();
		$(".leftmiddleFrame").load("leftside.html", function() {
	        $("#clientLeftMenu").show();
	    });
		$(".rightmiddleFrame").show().load("rateoverridden.html"); 
		}
	});*/
 /*$("ul#therapist").one().on('click', 'li#clientpage', function(e){
	 
	  	e.stopImmediatePropagation();
	    e.preventDefault();
		var menuID = this.id;
		$(".rightmiddleFrame").empty();
	    $(".leftmiddleFrame").empty();
		if(menuID == "clientpage"){			
		//$(".leftmiddleFrame").show().load("leftside.html");
		$(".leftmiddleFrame").load("leftside.html", function() {
	        $("#clientLeftMenu").show();
	    });
		$(".rightmiddleFrame").show().load("client.html"); 
		}
	});*/
  /*$("ul#therapist ul").one().on('click', 'li#clientPayment', function(e){
	  
	  	e.stopImmediatePropagation();
	    e.preventDefault();
		var menuID = this.id;
		$(".rightmiddleFrame").empty();
	    $(".leftmiddleFrame").empty();
		if(menuID == "clientPayment"){
			
		//$(".leftmiddleFrame").show().load("leftside.html");
		$(".leftmiddleFrame").load("leftside.html", function() {
	        $("#clientLeftMenu").show();
	    });
		$(".rightmiddleFrame").show().load("clientpayment.html"); 
		}
	});*/
  $(" #cssmenu ul li.active ul > li#diag ").one().click(function(e){
	  var menuID = this.id;
	 
	    e.stopImmediatePropagation();
	    e.preventDefault();
	    if(menuID == "diag"){
	    	$(".rightmiddleFrame").empty();
		    $(".leftmiddleFrame").empty();
			$(".leftmiddleFrame").load("leftside.html", function() {
				        $("#diagnosisLeftMenu").show();
			});
			$.noConflict();
			$(".rightmiddleFrame").show().load("diagnosis.html"); 
			}
	});
  $(" #cssmenu ul li.active ul > li#acttype ").one().click(function(e){
	  var menuID = this.id;
	 
	    e.stopImmediatePropagation();
	    e.preventDefault();
	    if(menuID == "acttype"){
	    	$(".rightmiddleFrame").empty();
		    $(".leftmiddleFrame").empty();
			$(".leftmiddleFrame").load("leftside.html", function() {
				        $("#actTypeLeftMenu").show();
			});
			$(".rightmiddleFrame").show().load("activitytype.html"); 
			}
	});
  $(" #cssmenu ul li.active ul > li#ref ").one().click(function(e){
	  var menuID = this.id;
	 
	    e.stopImmediatePropagation();
	    e.preventDefault();
	    if(menuID == "ref"){
	    	$(".rightmiddleFrame").empty();
		    $(".leftmiddleFrame").empty();
			$(".leftmiddleFrame").load("leftside.html", function() {
				        $("#refSourceLeftMenu").show();
			});
			$(".rightmiddleFrame").show().load("referralsource.html"); 
			
			}
	});
  $(" #cssmenu ul li.active ul > li#flag ").one().click(function(e){
	  var menuID = this.id;
	 
	    e.stopImmediatePropagation();
	    e.preventDefault();
	    if(menuID == "flag"){
	    	$(".rightmiddleFrame").empty();
		    $(".leftmiddleFrame").empty();
			$(".leftmiddleFrame").load("leftside.html", function() {
				        $("#flagLeftMenu").show();
			});
			$(".rightmiddleFrame").show().load("flags.html"); 
			}
	});
  
  });
})(jQuery);

