( function( $ ) { 
	
	$(document).ready(function(e){	
		 
		var rooturl = "http://localhost:8181/TMS/service/resource/";
		var flagid ="";
		var doctorid =""
		var actTypeid ="";
		var diagnosisid ="";
		var flagDiag = false;
		var flagval = "";
		var refid = "";
		var aType = "";
		var cid = "";
		var tid = "";
		var pid ="";
		var aDateArr = ($(this).text()).split('-');
		var aDate = aDateArr[1];
		var JSONObject;
		var jsonData;
		var access = false;
		var counter = 2;
		var originalContents;
		
		window.onunload = unloadPage();
		function unloadPage()
		{
			sessionStorage.removeItem("diagcounter");
			sessionStorage.removeItem("activitycounter");
		}
		var formid = $('form').attr('id');
		
		var headerText = sessionStorage.getItem("theader");
		
		if(headerText == null  && formid == 'doctor'){
			$(".header").html("Add a New Therapist");
		}
		else if(headerText == null && formid == 'doctorrate'){
			$(".header").html("Add Therapist Rates");
		}
		else if(headerText == null && formid == 'client'){
			$(".header").html("Add a New Client");
		}
		else if(headerText == null && formid == 'overriderate'){
			$(".header").html("Add Rate Discount");
		}
		else{
		$(".header").html(headerText);
		}
		
		//To add new combo for Rates page 
		$(".addNew").one().click(function (e) {
			e.stopImmediatePropagation();
			e.preventDefault();


			if(this.name=='moreClass') {
				if(counter>9){
					alert("Only 9 Classes are allowed");
					return false;
				} 



				var newTextBoxDiv = $(document.createElement('div'))
				.attr("id", 'ComboDiv' + counter);
				//to add more class in rates page
				if(this.name == 'moreClass'){


					newTextBoxDiv.after().html('<div class="halfFrame"><div><div class="oneThirdFrame"><label>Class #'+ counter + ' : </label></div>' +
							'<div class="twoThirdFrame"><select class="activity'+counter+ '" id="acttypeid[]" name="acttypeid[]" ><option value="">Select ActivityType</option>' +						
							'</select></div></div></div>' +
							'<div class="halfFrame"><div><div class="twoThirdFrame"><select id="report" name="selectrate[]"><option value="">Rate Per</option>' +
							'<option value="Hour">Hour</option><option value="Activity">Activity</option><option value="Months">Months</option><option value="year">% per year</option></select>'+
							'<label>Unit:</label><input class="smallTxtbox" type="text" id="valrateper[]" name="valrateper[]" maxlength="3"/></div>' +
							'<div class="oneThirdFrame"><label>$</label><input class="rateTxtbox" placeholder="rate $" type="text" name="docamt[]" id="docamt[]" value="" ></div></div></div>'

					);


					newTextBoxDiv.appendTo(".classGroup");
				}
				
				sessionStorage.setItem("activitycounter",counter);
				
				getActivityType();
				counter++;
			}
			else if(this.name == 'moreClassOver'){

				if(counter>9){
					alert("Only 9 Classes are allowed");
					return false;
				} 
				var newTextBoxDiv = $(document.createElement('div'))
				.attr("id", 'ClientComboDiv' + counter);
				if(this.name == 'moreClassOver'){				

					newTextBoxDiv.after().html('<div class="halfFrame"><div><div class="oneThirdFrame"><label>Class #'+ counter + ' : </label></div>' +
							'<div class="twoThirdFrame"><select class="activity'+counter+ '"id="acttypeid[]" name="acttypeid[]" ><option value="">Select ActivityType</option>' +						
							'</select></div></div></div>' +
							'<div class="halfFrame"><div><div class="twoThirdFrame"><select id="report" name="selectrate[]"><option value="">Rate Per</option>' +
							'<option value="Hour">Hour</option><option value="Activity">Activity</option><option value="Months">Months</option><option value="year">% per year</option></select>'+
							'<label>Unit:</label><input class="smallTxtbox" type="text" id="valrateper[]" name="valrateper[]" maxlength="3"/></div></div>' +
							'<div class="oneThirdFrame"><label>$</label><input class="rateTxtbox" placeholder="rate $" type="text" name="docamt[]" id="docamt[]" value="" ></div>' +
							'<div class="halfFrame"><div><div class="twoThirdFrame"><label>$</label><input class="rateTxtbox" placeholder="waiver $" type="text" name="waive[]' + 
							'" id="waive[]" value="" ></div></div></div>' +
							'<div class="halfFrame"><div><div class="oneThirdFrame"></div><div class="twoThirdFrame">'+
							'<label>$</label><input class="rateTxtbox" placeholder="netcharge $" type="text" name="netcharge[]" id="netcharge[]" disabled value="" ></div></div></div>'
					);

					newTextBoxDiv.appendTo(".classGroup");
				}
				sessionStorage.setItem("activitycounter",counter);
				getActivityType();
				counter++;
			}
			else if(this.name == 'moreCode'){

				if(counter>5){
					alert("Only 5 Codes are allowed");
					return false;
				}   
				
				var newTextBoxDiv = $(document.createElement('div'))
				.attr("id", 'codeDiv' + counter);

				newTextBoxDiv.after().html('<div class="oneThirdFrame"></div><div class="twoThirdFrame"><select class="diag'+counter+'"name="diagnosisc[]" id="diagnosisc[]"><option value="">Select Diagnoses Code</option>' +
				'</select></div>');

				newTextBoxDiv.appendTo(".addMoreCodeGroup");
				/*$("select[name*='diagnosisc'] option:gt(0)").remove();
				$("select[name*='diagnosisc']").append(new Option("Select Diagnosis Code",""));
				
			*/	
				
			 /*	var $options = $("select[name*='diagnosisc'] > option").clone();
			 	$("select[name*='diagnosisc']").append($options);*/
				sessionStorage.setItem("diagcounter",counter);
				getDiagnosis();	
				counter++;
			}

		});
		
	
		
		$(".btnEdit").click(function (e) {
			
			e.stopImmediatePropagation();
		    e.preventDefault();
		    $(".btnDelete").attr('enabled','enabled');
			
		});
		
		
			
			//To remove the dynamically added select boxes
		$(".removeButton").one().click(function (e) {
			e.stopImmediatePropagation();
			e.preventDefault();
			if(this.name=='removeClass'){
				if(counter==1){
					alert("No more Combo Box to remove");
					return false;
				}   

				counter--;

				$("#ComboDiv" + counter).remove();
			}
			else if(this.name=='removeClassOver'){
				if(counter==1){
					alert("No more Classes to remove");
					return false;
				}   

				counter--;

				$("#ClientComboDiv" + counter).remove();
			}
			else if(this.name=='removeCode'){	
				if(counter==1){
					alert("No more Codes to remove");
					return false;
				}   

				counter--;

				$("#codeDiv" + counter).remove();
				$("#comboCode" + counter).remove();
			}


		});
     
	  
				//To populate Diagnosis Code
				
		function getDiagnosis(){
			
			$.ajax({
				type: 'POST',
				contentType: 'application/json',
				url: rooturl+ "fetchDiagnosisCode",
				async:false,
				dataType: "json",   			
				success: function (data, textStatus, jqXHR) {
					console.log(data);
					var diagcnt = sessionStorage.getItem("diagcounter");
					
					if(diagcnt == null){
						diagcnt = 1;
					}
						
						$.each(data, function(index, diagcode) {
							
							/*var opts = $("select[name*='diagnosisc'] ")[0].options;
							$(opts).val(function(i,diagcode){
								alert($(this).value );
								alert("Diag:"+diagcode.diagosisid);
								if($(this).value != diagcode.diagosisid){*/
									//$("select[name*='diagnosisc']").append(new Option(diagcode.diagcode,diagcode.diagnosisid)).eq(index);
							//$.each("select[name*='diagnosisc']", function(index, diagcode) {			
							
							var hasOption = $('.diag'+diagcnt+' option[value="' + diagcode.diagnosisid + '"]');
							
							if (hasOption.length == 0) {
								$('.diag'+diagcnt).append(new Option(diagcode.diagcode,diagcode.diagnosisid));
								//$("select[name*='diagnosisc']").append(new Option(diagcode.diagcode,diagcode.diagnosisid));
						    }
							
									
									//$("select[name*='acttypeid']").append(new Option(aType.activitytype,aType.typeid));
								//}
							});
						//});

							/*var opts = $("select[name*='acttypeid']")[key].options;
							
							$(opts).val(function(i,v){
								  $(this).siblings("[value='"+ v +"']").remove();
								});*/
							//var hasOption = $("select[name*='diagnosisc'] option[value='" + diagcode.diagnosisid + "']");
							/*if (hasOption.length == 0) {
							$("select[name*='diagnosisc']").append(new Option(diagcode.diagcode,diagcode.diagnosisid)).eq(index);
							}*/
						
						
						/*$("select[name*='diagnosisc']").each(function(index,diagcode){
							var opts = $('select[name*="diagnosisc"]')[index].options;
						
							$($('select[name*="diagnosisc"]')[index].options).val(function(i,diagcode){
								  $(this).siblings("[value='"+ diagcode.diagnosisid +"']").remove();
								});
						});	*/
						
						
					/*	var hasOption = $('#therapistName option[value="' + therapist.therapistid + '"]');
						if (hasOption.length == 0) {
							$("#therapistName").append(new Option(name,therapist.therapistid));
					    }*/
						
						
					

				},
				error: function(jqXHR, x, errorThrown){
					renderErrorMessage(jqXHR, x, errorThrown);
				}
			});
		}
		
		//To populate Client
		function getClient(){


			$.ajax({
				type: 'POST',
				contentType: 'application/json',
				url: rooturl+ "fetchClient",
				async:false,
				dataType: "json",   			
				success: function (data, textStatus, jqXHR) {
						if(formid == 'jasper')
						{
							if ( $("#selectClient  option[value='0']").length == 0 )
								$("#selectClient").append(new Option( "All Clients", 0));
							
						}	
					$.each(data, function(index, name) {
						var hasOption = $('#selectClient option[value="' + name.nameid + '"]');
						if (hasOption.length == 0) {
							$("#selectClient").append(new Option(name.lastname+","+name.firstname + " " +name.middlename, name.nameid));
					    }
						
										
					});
					

				},
				error: function(jqXHR, x, errorThrown){
					renderErrorMessage(jqXHR, x, errorThrown);
				}
			});
		}
			
			//To populate Therapist List
			
		function getTherapist(){
			$.ajax({
				type: 'POST',
				contentType: 'application/json',
				url: rooturl+ "fetchTherapist",
				async:false,
				dataType: "json",   			
				success: function (data, textStatus, jqXHR) {
					console.log(data);
					
					$.each(data, function(index, therapist) {
						var name =  therapist.lastname +","+ therapist.firstname +""+ therapist.middlename;
						var hasOption = $('#therapistName option[value="' + therapist.therapistid + '"]');
						if (hasOption.length == 0) {
							$("#therapistName").append(new Option(name,therapist.therapistid));
					    }
						

					});  
				

				},
				error: function(jqXHR, x, errorThrown){
					renderErrorMessage(jqXHR, x, errorThrown);
				}
			});
		}
		function getSearchTherapist(){
			
			$.ajax({
				type: 'POST',
				contentType: 'application/json',
				url: rooturl+ "fetchTherapist",
				async:false,
				dataType: "json",   			
				success: function (data, textStatus, jqXHR) {
					console.log(data);
					
					$.each(data, function(index, therapist) {
						var name =  therapist.lastname +","+ therapist.firstname +""+ therapist.middlename;
						var hasOption = $('#searchtherapistName option[value="' + therapist.therapistid + '"]');
						if (hasOption.length == 0) {
							$("#searchtherapistName").append(new Option(name,therapist.therapistid));
					    }
						

					});  
				

				},
				error: function(jqXHR, x, errorThrown){
					renderErrorMessage(jqXHR, x, errorThrown);
				}
			});
		}
			
			//To populate Flag
		function getFlag(){


			$.ajax({
				type: 'POST',
				contentType: 'application/json',
				url: rooturl+ "fetch",
				async:false,
				dataType: "json",   			
				success: function (data, textStatus, jqXHR) {
					
					$.each(data, function(index, flag) {
						var hasOption = $('#selectFlag option[value="' + flag.flagid + '"]');
						if (hasOption.length == 0) {
							$("#selectFlag").append(new Option(flag.flagname, flag.flagid));
					    }
						
										
					});
					
					/*$("#selectFlag option").val(function(i,v){
						
						  $(this).siblings("[value='"+ v +"']").remove();
						});*/
					
					/*var options = $('#selectFlag option');
					$(options).each(function(i,v){
						alert(options.value);
						});*/

				},
				error: function(jqXHR, x, errorThrown){
					renderErrorMessage(jqXHR, x, errorThrown);
				}
			});
		}
		
		function getClientDetails()
		{
			var clientnameid = sessionStorage.getItem('cnameid');
			
			$.ajax({
				type: 'POST',
				contentType: 'application/json',
				url: rooturl+ "fetchClientDetails/" + clientnameid,
				async:false,
				dataType: "json",   			
				success: function (data, textStatus, jqXHR) {
					console.log(data);
					
					var theader = data.firstname +" " + data.lastname;
					sessionStorage.setItem("theader",theader);
					sessionStorage.setItem("cclientid",data.clientid);
					sessionStorage.setItem("cfirstname",data.firstname);
					sessionStorage.setItem("clastname",data.lastname);
					sessionStorage.setItem("cmiddlename",data.middlename);
					//sessionStorage.setItem("cprefix",data.prefix)
					sessionStorage.setItem("csex",data.sex);
					sessionStorage.setItem("caddress1",data.address1);
					sessionStorage.setItem("caddress2",data.address2);
					sessionStorage.setItem("ccity",data.city);
					sessionStorage.setItem("czip",data.zip);
					sessionStorage.setItem("cstate",data.state);
					sessionStorage.setItem("chomephone",data.homephone);
					sessionStorage.setItem("cworkphone",data.workphone);
					sessionStorage.setItem("cmobile",data.mobile);
					sessionStorage.setItem("cactivity",data.acttypeid);
					sessionStorage.setItem("cselectFlag",data.flagid);
					sessionStorage.setItem("ccpt",data.cpt);
					sessionStorage.setItem("cdob",data.dob);
					sessionStorage.setItem("cdov",data.dov);
					sessionStorage.setItem("cstheraphy",data.stheraphy);
					sessionStorage.setItem("cpayment",data.payment);
					sessionStorage.setItem("ctherapistid",data.therapistid);
					sessionStorage.setItem("creferral",data.referralid);
					sessionStorage.setItem("cdiagnosiscd",data.diagnosiscode);

				},
				error: function(jqXHR, x, errorThrown){
					renderErrorMessage(jqXHR, x, errorThrown);
				}
			});
		}
			
		function getReferralSources(){
			//To populate Referral sources


			$.ajax({
				type: 'POST',
				contentType: 'application/json',
				url: rooturl+ "fetchReferralSource",
				async:false,
				dataType: "json",   			
				success: function (data, textStatus, jqXHR) {
					console.log(data);
					
					$.each(data, function(index, refsource) {
						var hasOption = $('#referral option[value="' + refsource.referralid + '"]');
						if (hasOption.length == 0) {
							$("#referral").append(new Option(refsource.referralsource,refsource.referralid));
					    }
						

					}); 
					//to avoid duplicates
				/*	$("#referral option").val(function(i,v){
						  $(this).siblings("[value='"+ v +"']").remove();
						});*/

				},
				error: function(jqXHR, x, errorThrown){
					renderErrorMessage(jqXHR, x, errorThrown);
				}
			});
		}
			
		function getSearchActivityType(){
			
			$.ajax({
				type: 'POST',
				contentType: 'application/json',
				url: rooturl+ "fetchActivityType",
				async:false,
				dataType: "json",   			
				success: function (data, textStatus, jqXHR) {
					console.log("Activity Type:"+data);
					var activitycnt = sessionStorage.getItem("activitycounter");
					
					if(activitycnt == null){
						activitycnt = 1;
					}
					//localStorage.clear();
					$.each(data, function(index, aType) {
						
							var hasOption = $('#csearchactivity option[value="' + aType.typeid + '"]');
							if (hasOption.length == 0) {
								$("#csearchactivity").append(new Option(aType.activitytype,aType.typeid));
						    }
						
					}); 

				},
				error: function(jqXHR, x, errorThrown){
					renderErrorMessage(jqXHR, x, errorThrown);
				}
			});
		}
		
function getActivityType(){
			
			$.ajax({
				type: 'POST',
				contentType: 'application/json',
				url: rooturl+ "fetchActivityType",
				async:false,
				dataType: "json",   			
				success: function (data, textStatus, jqXHR) {
					console.log("Activity Type:"+data);
					var activitycnt = sessionStorage.getItem("activitycounter");
					
					if(activitycnt == null){
						activitycnt = 1;
					}
					//localStorage.clear();
					$.each(data, function(index, aType) {
						
						//localStorage.setItem(aType.typeid, aType.activitytype);
						if(formid == 'client' || formid == 'payments' || formid == 'newactivity' )	{
							var hasOption = $('#cactivity option[value="' + aType.typeid + '"]');
							if (hasOption.length == 0) {
								$("#cactivity").append(new Option(aType.activitytype,aType.typeid));
						    }
							
						}else{
							var hasOption = $('.activity'+activitycnt+' option[value="' + aType.typeid + '"]');
							
							if (hasOption.length == 0) {
								$('.activity'+activitycnt).append(new Option(aType.activitytype,aType.typeid));
								//$("select[name*='diagnosisc']").append(new Option(diagcode.diagcode,diagcode.diagnosisid));
						    }
						}
							//$("select[name*='acttypeid']").append(new Option(aType.activitytype,aType.typeid));
					}); 
				
/*					
					$("select[name*='acttypeid']").each(function(key,val){
						var opts = $("select[name*='acttypeid']")[key].options;
					
						$(opts).val(function(i,v){
							  $(this).siblings("[value='"+ v +"']").remove();
							});
					});*/
					

				},
				error: function(jqXHR, x, errorThrown){
					renderErrorMessage(jqXHR, x, errorThrown);
				}
			});
		}
		
		
		if(formid == 'newactivity'){
			
			getSearchActivityType();
			getSearchTherapist();
			getClient();
		}
		
		//New Activity page load
		if(formid == 'newactivity'){
		
			getActivityType();
			getTherapist();
			var acttype ="";
			var therapistname ="";
			var cnameid = sessionStorage.getItem("cnameid");
			var clientname =sessionStorage.getItem("cactivityname");
			$('#cname').val(clientname);
			$('#report').attr("disabled", true);
			$('#rateperval').attr("disabled", true);
			$('#rate').attr("disabled", true);
			
					$('#cactivity').change(function(e){
						
							acttype = $(this).val();
							therapistname = $('#therapistName').val();
							if(therapistname != "" && acttype != ""){
								getClientActivityRate();
							}
					});
					
					$('#therapistName').change(function(e){
						
							therapistname = $(this).val();
					
					acttype = $('#cactivity').val();
					
					if(therapistname != "" && acttype != ""){
						getClientActivityRate();
					}
					});
					
					$('#waiver').keyup(function(){
						var rate = $('#rate').val();
						var waiver = Number($(this).val());
						var netch = rate - waiver;
						$('#netch').val(netch);
						
					});
		}
		
		function getClientActivityRate(){
			$.ajax({
				type: 'GET',
				contentType: 'application/json',
				url: rooturl+ "clientNewActivityRateGet" + "/" + therapistname + "/" +acttype +"/"+ cnameid,
				async:false,
				dataType: "json",   			
				success: function (data, textStatus, jqXHR) {
					console.log(data);
						$('#report').val(data.rateper);
						$('#rateperval').val(data.rateperval);
						$('#rate').val(data.camount);
						$('#waiver').val(data.newactwaiver);
						$('#netch').val(data.newactnetcharge);
				},
				error: function(jqXHR, x, errorThrown){
					renderErrorMessage(jqXHR, x, errorThrown);
				}
			});
		}
		//Client Page Validation
		if(formid == 'client'){
			getDiagnosis();
			getFlag();
			getActivityType();
			getReferralSources();
			getTherapist();

			sessionStorage.removeItem("theader");
			sessionStorage.removeItem("tfirstname");
			sessionStorage.removeItem("tlastname");
			sessionStorage.removeItem("tmiddlename");
			
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
			sessionStorage.removeItem("tarrtActivityType");
			sessionStorage.removeItem("tarrtRateperVal");
			sessionStorage.removeItem("tarrtRate");
			sessionStorage.removeItem("tarrtRateper");
			//sessionStorage.removeItem("tmark");
			//To display clientdetails
			
			$('#firstname').val(sessionStorage.getItem("cfirstname"));
			$('#lastname').val(sessionStorage.getItem("clastname"));
			$('#middlename').val(sessionStorage.getItem("cmiddlename"));
			//$('#prefix').val(sessionStorage.getItem("cprefix"));
			//alert(sessionStorage.getItem("csex"));
			$("input[name=sex][value=" + sessionStorage.getItem("csex") + "]").attr('checked', 'checked');
			//$('#sex').val(sessionStorage.getItem("csex")).attr('checked',true);
			$('#address1').val(sessionStorage.getItem("caddress1"));
			$('#address2').val(sessionStorage.getItem("caddress2"));
			$('#city').val(sessionStorage.getItem("ccity"));
			$('#zip').val(sessionStorage.getItem("czip"));
			$('#state').val(sessionStorage.getItem("cstate"));
			$('#homephone').val(sessionStorage.getItem("chomephone"));
			$('#workphone').val(sessionStorage.getItem("cworkphone"));
			$('#mobile').val(sessionStorage.getItem("cmobile"));
			$('#cactivity').val(sessionStorage.getItem("cactivity"));
			$('#selectFlag').val(sessionStorage.getItem("cselectFlag"));
			$('#cpt').val(sessionStorage.getItem("ccpt"));
			$('#dob').val(sessionStorage.getItem("cdob"));
			$('#dov').val(sessionStorage.getItem("cdov"));
			$('#stheraphy').val(sessionStorage.getItem("cstheraphy"));
			$('#payment').val(sessionStorage.getItem("cpayment"));
			$('#therapistName').val(sessionStorage.getItem("ctherapistid"));
			$('#referral').val(sessionStorage.getItem("creferral"));
			
			if(sessionStorage.getItem("cfirstname") != null){
				$(".btnSave").attr('value', 'Update');
				$(".btnCancel").attr('value', 'Next');
				$(".btnDelete").attr('disabled', false);
			}
			else
				$(".btnDelete").attr('disabled', true);
			if(sessionStorage.getItem("cdiagnosiscd") != null){
				var cdiagnosiscd = sessionStorage.getItem("cdiagnosiscd");
				var carrdiagnosiscd = cdiagnosiscd.split(",");
				
				var clickEvent = (carrdiagnosiscd.length - 1);
				
				for (var i = 0; i < clickEvent; i++) {
				$('.addNew').trigger('click');
				}
				getDiagnosis();
					$.each(carrdiagnosiscd, function(key,value){
						$('select[name="diagnosisc[]"]').eq(key).val(value);

			});
					
			}
		}
		
		if(formid == 'doctor' ){
			getFlag();

			$('#firstname').val(sessionStorage.getItem("tfirstname"));
			$('#lastname').val(sessionStorage.getItem("tlastname"));
			$('#middlename').val(sessionStorage.getItem("tmiddlename"));
			$('#address1').val(sessionStorage.getItem("taddress1"));
			$('#address2').val(sessionStorage.getItem("taddress2"));
			$('#city').val(sessionStorage.getItem("tcity"));
			$('#zipcode').val(sessionStorage.getItem("tzipcode"));
			$('#state').val(sessionStorage.getItem("tstate"));
			$('#homephone').val(sessionStorage.getItem("thomephone"));
			$('#workphone').val(sessionStorage.getItem("tworkphone"));
			$('#mobile').val(sessionStorage.getItem("tmobile"));
			$('#id').val(sessionStorage.getItem("tid"));
			$('#ein').val(sessionStorage.getItem("tein"));
			$('#license').val(sessionStorage.getItem("tlicense"));
			$('#selectFlag').val(sessionStorage.getItem("tflag")).attr('selected',true);		
			//$('#mark').prop('checked', sessionStorage.getItem("tmark"));
			if(sessionStorage.getItem("tfirstname") != null){
				$(".btnSave").attr('value', 'Update');
				$(".btnCancel").attr('value', 'Next');
				$(".btnDelete").attr('disabled', false);
			}
			var tActivityType = sessionStorage.getItem('tarrtActivityType');
			console.log("The array value:"+sessionStorage.getItem('tarrtActivityType'));
			
		}
		
		if(formid == 'reportym' || formid == 'jasper'){
			getFlag();
			getClient();
			getTherapist();
		}
		
			
		if(formid == 'doctorrate' || formid =='overriderate'){
			var tarractivitytype = sessionStorage.getItem("tarrtActivityType");
			var tarrtActivityTypearr = tarractivitytype.split(",");
			var clickEvent = (tarrtActivityTypearr.length - 1);
			getActivityType();
			
			for (var i = 0; i < clickEvent; i++) {
				$('.addNew').trigger('click');
			}
			
			$.each(tarrtActivityTypearr, function(key,value){

				// $("select[name*='acttypeid']").val(value);
				$('select[name="acttypeid[]"]').eq(key).val(value);

			});

			var rateperval = sessionStorage.getItem("tarrtRateperVal");
			var ratepervalarr = rateperval.split(",");
			
			$.each(ratepervalarr, function(key,value){

				$('input[name="valrateper[]"]').eq(key).val(value);

			});

			var trate = sessionStorage.getItem("tarrtRate");
			var tarrtRate = trate.split(",");
			$.each(tarrtRate, function(key,value){
				//$("input[name*='docamt']").val(value);
				$('input[name="docamt[]"]').eq(key).val(value);
			});



			var rateper = sessionStorage.getItem("tarrtRateper");
			rateperarray = rateper.split(",");

			$.each(rateperarray, function(key,value){

				$('select[name="selectrate[]"]').eq(key).val(value);
			});
			if(formid == 'doctorrate'){
				var checkVal = sessionStorage.getItem("tarrtActivityType");
				
				if (checkVal != "" )
					$(".btnSave").attr('value', 'Update');
					
				
				}
			else if(formid == 'overriderate'){
				var waiver= sessionStorage.getItem("cwaiver");
				waiverarray = waiver.split(",");
				
				$('.btnSave').attr("value","Update");
				$.each(waiverarray, function(key,value){

					$('input[name="waive[]"]').eq(key).val(value);
				});
				
				var netch = sessionStorage.getItem("cnetch");
				netcharray = netch.split(",");
				
				$.each(netcharray, function(key,value){
					
					$('input[name="netcharge[]"]').eq(key).val(value);
				});
				$('select[name="acttypeid[]"]').attr("disabled", true);
				$('select[name="selectrate[]"]').attr("disabled", true);
				$('input[name="valrateper[]"]').attr("disabled", true);
				$('input[name="docamt[]"]').attr("disabled", true);
			}


		}
		if(formid == 'payments'){
			getActivityType();
			getTherapist();
		}
		
		
		$('input[name="waive[]"]').keyup(function(){
			   var subval = 0;
			   var rate = 0;
			   var waiver = 0;
			   $('input[name="waive[]"]').each(function(index) {
			               // val += Number($(this).val());
			                rate =  $('input[name="docamt[]"]').eq(index).val();
			                waiver = Number($(this).val());
			                subval = rate - waiver; 
						    $('input[name="netcharge[]"]').eq(index).val(subval);
			                	
			               
			            });
			    
			});
		
		$('.btnCancel').click(function(e){
			
			var buttonval = $(this).val();
			
			var therapistid ="";
			var urlvalue="";
			var ctherapistid ="";
			var clientid ="";
			if(buttonval == 'Cancel' && formid == 'doctor'){
				$("#doctor").trigger('reset');
			}
			else if(buttonval == 'Cancel' && formid == 'doctorrate'){
				$("#doctorrate").trigger('reset');
			}
			else if(buttonval == 'Cancel' && formid == 'overriderate'){
				$("#overriderate").trigger('reset');
			}
			else if(buttonval == 'Cancel' && formid == 'client'){
				$("#client").trigger('reset');
			}
			if(buttonval == 'Next'){

				if(formid == 'doctor'){
					therapistid = sessionStorage.getItem("ttherapistid");
					urlvalue = rooturl+ "therapistRateGet" + "/" + therapistid;
				}
				else if(formid == 'client'){
					
					clientid = sessionStorage.getItem("cclientid");
					therapistid = sessionStorage.getItem("ctherapistid");
					
					var urlvalue = rooturl+ "clientRateGet" + "/" + therapistid +"/" +clientid;
				}

				$.ajax({

					type: 'GET',
					contentType: 'application/json',
					url: urlvalue,
					async:false,
					dataType: "json",   			
					success: function (data, textStatus, jqXHR) {
						
						if(formid == "doctor"){
							renderRateDetails(data);
							$(".rightmiddleFrame").show().load("therapistrate.html"); 
							
							var rateper = sessionStorage.getItem("tarrtRateper");
							
						}else if(formid == "client"){
							renderClientRateDetails(data);
							$(".rightmiddleFrame").show().load("rateoverridden.html"); 

						}
					},
					error: function(jqXHR, x, errorThrown){
						renderErrorMessage(jqXHR, x, errorThrown);
					}
				});

				function renderRateDetails(data){
					console.log(data);
					sessionStorage.setItem("tarrtRateper",data.arrtRateper);
					sessionStorage.setItem("tarrtRateperVal",data.arrtRateperVal);
					sessionStorage.setItem("tarrtActivityType",data.arrtActivityType);
					sessionStorage.setItem("tarrtRate",data.arrtRate);
					
				}
				function renderClientRateDetails(data){
				
					sessionStorage.setItem("tarrtRateper",data.arrtRateper);
					sessionStorage.setItem("tarrtRateperVal",data.arrtRateperVal);
					sessionStorage.setItem("tarrtActivityType",data.arrtActivityType);
					sessionStorage.setItem("tarrtRate",data.arrtRate);
					sessionStorage.setItem("cwaiver",data.waiver);
					sessionStorage.setItem("cnetch",data.netch);
					var rateheader = data.firstname + " " + data.lastname;
					sessionStorage.setItem("theader",rateheader);
					
				}


			}

		});
		
		
		
		$('.btnGen').click(function(e){
			console.clear();
			$("#successmessage").hide();
			$("#errormessage").hide().html("");
			$('#jasper').validate({
				rules: {
					repFor:{
						required: true
						},
				fromdate:{
						required: true,
						date : true
					},
					todate:{
						required: true,
						date : true
					},
					selectClient:{
						required: true
					},
					therapistName:{
						required: true
					}			
					
					
				},
				messages: {
					repFor: "*",
					fromdate: "*",
						todate:{
							required: "*"
						},
						therapistName: "*",
						selectClient:"* Required Field"
				},
				submitHandler: function(form) {
					
					e.preventDefault();
					var startDt=$('#fromdate').val();
					var endDt=$('#todate').val();
					var postUrl;
					var flag;

					if(startDt < endDt){
						
						var stdt = startDt.split("-");
						var stdtyear= stdt[0];
						var edt = endDt.split("-");
						var edtyear= edt[0];
						if(stdtyear >= 2016 && edtyear >= 2016){
							
							if((($('#repFor').val() == 'rbi') || ($('#repFor').val() == 'rbs') ) && $('#selectClient').val() != 0)	
							{
							
							if($('#selectFlag').val() == '')
								flag = 0;
							else
								flag =$('#selectFlag').val();
							
							
							JSONObject= {"fromdate":startDt, "todate":endDt,"therapistid":$('#therapistName').val(),"nameid":$('#selectClient').val(),"flagid":flag};
							jsonData = JSON.stringify(JSONObject);  
							if($('#repFor').val() == 'rbi'	)
								postUrl = rooturl + "generateReportBI";
							else if($('#repFor').val() == 'rbs')
								postUrl = rooturl + "generateReportBS";
							
							
						
								$.ajax({
								type: 'POST',
								contentType: 'application/json',
								url: postUrl,
								async:false,
								dataType: "json", 
								data: jsonData,
								success: function(data, textStatus, error){ 
									console.log("Value of data:"+data);
									if($('#repFor').val() == 'rbi')
										renderReportInsurance(data,textStatus,error);
									else if($('#repFor').val() == 'rbs')
										renderReportSummary(data,textStatus,error);	
										
									
									// renderReport(data,textStatus,error);
									//$('.btnAdd').attr('disabled', false);
								},
								error: function(jqXHR, x, errorThrown){
									renderErrorMessage(jqXHR, x, errorThrown);
									$("#successmessage").hide();
									$("#errormessage").show().html("");
									$("#errormessage").show().html("Error:"+errorThrown);
								}
							});
						}
							else if(($('#repFor').val() == 'rtm') || ($('#repFor').val() == 'rcl') || ($('#repFor').val() == 'rt')){
								
								if($('#selectFlag').val() == '')
									flag = 0;
								else
									flag =$('#selectFlag').val();
								
								
								JSONObject= {"fromdate":startDt, "todate":endDt,"therapistid":$('#therapistName').val(),"nameid":$('#selectClient').val(),"flagid":flag};
								jsonData = JSON.stringify(JSONObject);  
								if($('#repFor').val() == 'rtm')				
									 postUrl = rooturl + "generateReportTM";
								else if($('#repFor').val() == 'rcl')
									postUrl = rooturl + "generateReportCL";
								else if($('#repFor').val() == 'rt')
									postUrl = rooturl + "generateReportTOT";
								
								
								$.ajax({
								type: 'POST',
								contentType: 'application/json',
								url: postUrl,
								async:false,
								dataType: "json", 
								data: jsonData,
								success: function(data, textStatus, error){ 
									console.log("Value of data:"+data);
									if($('#repFor').val() == 'rtm')				
										renderReportTM(data,textStatus,error);	
									else if($('#repFor').val() == 'rcl')
										renderReportCL(data,textStatus,error);	
									else if($('#repFor').val() == 'rt')
										renderReportTOT(data,textStatus,error);	
									
										
									
									
								},
								error: function(jqXHR, x, errorThrown){
									renderErrorMessage(jqXHR, x, errorThrown);
									$("#successmessage").hide();
									$("#errormessage").show().html("");
									$("#errormessage").show().html("Error:"+errorThrown);
								}
							});
								
								
							}
						else{
							$("#successmessage").hide();
							$("#errormessage").show().html("");
							$("#errormessage").show().html("Bill Insurance Report can be generated only for an Individual Client.");

						}
							
						}
						else{
							$("#successmessage").hide();
							$("#errormessage").show().html("");
							$("#errormessage").show().html("Reports can be generated only for the year 2016 or after. Please enter the correct dates.");
					
						}
						
					}
					else{
						$("#successmessage").hide();
						$("#errormessage").show().html("");
						$("#errormessage").show().html("Please enter a From date lesser than To date");
					}
					
					
				}
				
			});
			
		});
		

		$('.btnSave').click(function(e){
			
			var buttonval = $(this).val();
			var urlvalue ="";
			$('#doctor').validate({
				rules: {
					firstname: "required",
					lastname: "required",        		 
					address1: "required",
					city: "required",
					mobile:{
						required: true,
						phoneUS: true
					},
					zipcode: {
						required: true,
						zipcodeUS: true
					},
					state:{
						required: true
					},
					flag:{
						required: true
					},
					id:{
						required: true,
						number: true,
						minlength: 10,
						maxlength: 10
					},
					ein:{
						required: true,
						number: true,
						minlength: 9,
						maxlength: 9
					},
					license:{
						required: true,
						alphanumeric : true,
						maxlength: 20
					},
					selectFlag:{
						required: true
					}
				},
				messages: {
					firstname: "*",            	 
					lastname: "*",
					address1: "*",
					city:     "*",
					mobile:{
						required: "*",
					},
					zipcode:{
						required: "*",
					},
					state:{
						required: "*",
					},
					id:{
						required: "*",
						minlength: "10 digits only please",
						maxlength: "10 digits only please"
					},
					ein:{
						required: "*",
						minlength: "9 digits only please",
						maxlength: "9 digits only please"
					},
					license:{
						required: "*",
					},
					flag:{
						required: "*",
					},
					selectFlag:{
						required: "*",
					}
				},
				submitHandler: function(form) {
					var form = $('#doctor');
					e.preventDefault();
					var nameid="";
					if(buttonval == 'Save'){
						urlvalue = rooturl + "therapist";
					}
					else if(buttonval == 'Update'){
						nameid = sessionStorage.getItem("nameid");
						urlvalue = rooturl + "therapistUpdate" +"/" + nameid;

					}
					JSONObject= {"firstname":$('#firstname').val(), "lastname":$('#lastname').val(),"middlename":$('#middlename').val(),
							"address1":$('#address1').val(),"address2":$('#address2').val(),
							"city":$('#city').val(),"zipcode":$('#zipcode').val(),"state":$('#state').val(),"homephone":$('#homephone').val(),
							"workphone":$('#workphone').val(),"mobile":$('#mobile').val(),"id":$('#id').val(),"ein":$('#ein').val(),
							"license":$('#license').val(),"flagid":$('#selectFlag').val()
					};
					jsonData = JSON.stringify(JSONObject); 

					$.ajax({
						type: 'POST',
						contentType: 'application/json',
						url: urlvalue,
						async:false,
						dataType: "json",  
						data: jsonData,
						success: function(data, textStatus, error){ 
							if(buttonval == 'Save'){
								$("#errormessage").hide();
								$("#successmessage").show().html('');
								$("#successmessage").show().html("Saved Successfully.Please Fill in the Rates");
								$(".btnCancel").attr("value","Next");
							}
							else if(buttonval == 'Update'){
								$("#errormessage").hide();
								$("#successmessage").show().html('');
								$("#successmessage").show().html("Updated Successfully.");
							}
							$("#doctor :input").attr("disabled", true);
							$(".btnCancel").attr("disabled",false);
							console.log("Data after update:"+data);
							doctorid = data.therapistid;
							sessionStorage.setItem("ttherapistid",  doctorid);
							//$(".rightmiddleFrame").show().load("therapistrate.html"); 
							var rateheader = $('#firstname').val() +" "+ $('#lastname').val();
							sessionStorage.setItem("theader",  rateheader);
						},
						error: function(jqXHR, x, errorThrown){
							renderErrorMessage(jqXHR, x, errorThrown);
						}

					});

				}

			});
			if(formid == 'doctorrate')	{
				var buttonvalue = $(this).attr("value");

				var urlvalue ="";
				$('#doctorrate').validate({

					rules: {

						"acttypeid[]": "required",
						"selectrate[]": "required",
						"valrateper[]":{
							required: true,
							number: true	
						},
						"docamt[]":{
							required: true,
							number: true	
						}

					},
					messages:{

						"acttypeid[]": "*",
						"selectrate[]": "*",
						"valrateper[]": "*",
						"docamt[]":"*"
					},
					submitHandler: function(form) {
						e.preventDefault();


						var acttypeid = [];
						$("select[name*='acttypeid']").each(function(){
							var val = $(this).val();
							if(val !== '') acttypeid.push(val);  
						});
						console.log(acttypeid);

						var selectrate = [];
						$("select[name*='selectrate']").each(function(){
							var val = $(this).val();
							if(val !== '') selectrate.push(val);  
						});
						console.log(selectrate);					

						var docamt = [];
						$("input[name*='docamt']").each(function(){
							var val = $(this).val();
							if(val !== '') docamt.push(val);  
						});
						console.log(docamt);

						var valrateper = [];
						$("input[name*='valrateper']").each(function(){
							var val = $(this).val();
							if(val !== '') valrateper.push(val);  
						});
						console.log(valrateper);
						doctorid = sessionStorage.getItem("ttherapistid");

						//doctorid = $.urlParam('therapistid');
						//doctorid = $('#dochidden').val();

						JSONObject= {"therapistid":doctorid,"arrtActivityType":acttypeid, "arrtRateperVal": valrateper,"arrtRateper":selectrate,"arrtRate":docamt
						};
						jsonData = JSON.stringify(JSONObject); 

						if(buttonvalue == 'Update')
							urlvalue = rooturl + "therapistrateUpdate" +"/" + doctorid
							else if(buttonvalue == 'Save')
								urlvalue = rooturl + "therapistrate";

						$.ajax({
							type: 'POST',
							contentType: 'application/json',
							url: urlvalue,
							async:false,
							dataType: "json",  
							data: jsonData,
							success: function(data, textStatus, error){ 
								$("#doctorrate :input").attr("disabled", true);
								if(buttonvalue == 'Save'){
									$("#errormessage").hide();
									$("#successmessage").show().html('');
									$("#successmessage").show().html("Saved Successfully.");
								}else if(buttonvalue == 'Update'){
									$("#errormessage").hide();
									$("#successmessage").show().html('');
									$("#successmessage").show().html("Updated Successfully.");
								}
								},
							error: function(jqXHR, x, errorThrown){
								renderErrorMessage(jqXHR, x, errorThrown);

							}

						});


					}

				});
			}
			
			if(formid == 'newactivity'){
				var buttonvalue = $(this).attr("value");

				var urlvalue ="";
				var cnameid ="";
				$('#newactivity').validate({

					rules: {

						cactivity: "required",
						rateper: "required",
						cname: "required",
						payment: {
							required: true,
							number: true
						},
						rate: "required",
						waiver: "required",
						netch: "required",
						therapistName: "required",
						rateperval:{
							required: true,
							number: true	
						},
						actydate:{
							required: true
						}

					},
					messages:{

						cactivity: "*",
						rateper: "*",
						cname: "*",
						payment: "*",
						rate: "*",
						waiver: "*",
						netch: "*",
						therapistName: "*",
						rateperval:"*",
						actydate:"*"
					},
					submitHandler: function(form) {
						e.preventDefault();
						cnameid = sessionStorage.getItem("cnameid");
						var tid = $('#therapistName').val();
						

						JSONObject= {	"nameid":			cnameid,
										"cname":			$('#cname').val(), 
										"therapistid":		tid,
										"acttypeid":		$('#cactivity').val(), 
										"rateperval":   	$('#rateperval').val(),
										"rateper":			$('#report').val(),
										"newactwaiver":		parseFloat($('#waiver').val(),10).toFixed(2),
										"newactnetcharge":	parseFloat($('#netch').val(),10).toFixed(2),
										"camount":			parseFloat($('#rate').val(),10).toFixed(2),
										"payment":			parseFloat($('#payment').val(),10).toFixed(2),
										"paymentdate":		$('#actydate').val()
									};
						jsonData = JSON.stringify(JSONObject); 
						
						
							if(buttonvalue == 'Save')
								urlvalue = rooturl + "clientAddNewActivity";
							else if(buttonvalue == 'Update')
								urlvalue = rooturl + "clientUpdateNewActivity";
						
						$.ajax({
							type: 'POST',
							contentType: 'application/json',
							url: urlvalue,
							async:false,
							dataType: "json",  
							data: jsonData,
							success: function(data, textStatus, error){ 
								$("#newactivity :input").attr("disabled", true);
								if(buttonvalue == 'Save'){
									$("#errormessage").hide();
									$("#successmessage").show().html('');
									$("#successmessage").show().html("Saved Successfully.");
									}
								else if(buttonvalue == 'Update'){
									$("#errormessage").hide();
									$("#successmessage").show().html('');
									$("#successmessage").show().html("Updated Successfully.");
								}
							},
							error: function(jqXHR, x, errorThrown){
								renderErrorMessage(jqXHR, x, errorThrown);

							}

						});


					}

				});
			}

			if(formid =='client'){
				
				$('#client').validate({
					rules: {
						firstname: "required",
						lastname: "required", 
						address1: "required",
						city: "required",
						mobile:{
							required: true,
							phoneUS: true
						},
						zip: {
							required: true,
							zipcodeUS: true
						},
						state:{
							required: true
						},
						selectFlag: {
							required: true
						},
						act:{
							required: true
						},
						cpt:{
							required: true
						},
						referral:{
							required: true
						},
						dob:{
							required: true,
							date: true
						},
						dov:{
							required: true,
							date: true
						},
						sex:{
							required: true
						},
						"diagnosisc[]":{
							required: true	
						},
						theraphysd:{
							required: true
						},
						stheraphy:{
							required: true,
							date: true
						},
						therapistName:{
							required: true
						},
						payment:{
							required: true
						},
						cactivity:{
							required: true
						}


					},

					messages: {
						firstname: "*",            	 
						lastname: "*",
						address1: "*",
						city:     "*",
						mobile:{
							required: "*",
						},
						zip:{
							required: "*",
						},
						state:{
							required: "*",
						},
						selectFlag:{
							required: "*",
						},
						act:{
							required: "*",
						},
						sex:{
							required: "*",
						},
						cpt:{
							required: "*",
						},
						dob:{
							required: "*",
						},
						dov:{
							required: "*",
						},
						referral:{
							required: "*",
						},
						diagnoses:{
							required: "*",
						},
						theraphysd:{
							required: "*",
						},
						stheraphy:{
							required: "*"
						},
						therapistName:{
							required: "*"	
						},
						payment:{
							required: "*"
						},
						"diagnosisc[]":{
							required: "*"	
						},
						cactivity:{
							required: "*"
						}


					},
					submitHandler: function(form) {
						e.preventDefault();
						var clientid="";
						var selectedSex ="";
						
						
						if(buttonval == 'Save'){
							urlvalue = rooturl + "client";
						}
						else if(buttonval == 'Update'){
							clientid = sessionStorage.getItem("cclientid");
							urlvalue = rooturl + "clientUpdate" +"/" + clientid;

						}
						var sex = $('input[name=sex]:checked', '#client').val();
					
						var diagnosisc= [];
						$("select[name*='diagnosisc']").each(function(){
							var val = $(this).val();
							if(val !== '') diagnosisc.push(val);  
						});

						JSONObject= {"firstname":$('#firstname').val(), "lastname":$('#lastname').val(),"middlename":$('#middlename').val(),
								"sex":sex,"address1":$('#address1').val(),"address2":$('#address2').val(),
								"city":$('#city').val(),"zip":$('#zip').val(),"state":$('#state').val(),"homephone":$('#homephone').val(),
								"workphone":$('#workphone').val(),"mobile":$('#mobile').val(),"activitytype":$('#cactivity').val(),"cpt":$('#cpt').val(),
								"diagnosiscode":diagnosisc,"dob":$('#dob').val(),"dov":$('#dov').val(),"flagid":$('#selectFlag').val(),"stheraphy":$('#stheraphy').val(),"payment":$('#payment').val(),
								"therapistid":$('#therapistName').val(),"referralid":$('#referral').val()
						};
						jsonData = JSON.stringify(JSONObject); 
						 
						$.ajax({
							type: 'POST',
							contentType: 'application/json',
							url: urlvalue,
							async:false,
							dataType: "json",  
							data: jsonData,
							success: function(data, textStatus, error){ 
								if(buttonval == 'Save'){
									$("#errormessage").hide();
									$("#successmessage").show().html('');
									$("#successmessage").show().html("Saved Successfully.Please Fill in the Rates");
									$('.btnCancel').attr("Value","Next");
								}
								else if(buttonval == 'Update'){
									$("#errormessage").hide();
									$("#successmessage").show().html('');
									$("#successmessage").show().html("Updated Successfully.");
								}
								$("#client :input").attr("disabled", true);
								$(".btnCancel").attr("disabled",false);
								console.log("Data after update:"+data);
								clientid = data.clientid;
								ctherapistid = data.therapistid;
								console.log("Client Therapist ID:"+ctherapistid);
								
								sessionStorage.setItem("cclientid",  clientid);
								sessionStorage.setItem("ctherapistid",  ctherapistid);
								 
								var rateheader = $('#firstname').val()+" "+$('#lastname').val();
								sessionStorage.setItem("theader",  rateheader);
							},
							error: function(jqXHR, x, errorThrown){
								renderErrorMessage(jqXHR, x, errorThrown);
							}

						});
					}

				});
			}

			if(formid == "searchdiagnosiscode"){

				$("#successmessage").hide();
				var buttonValue= $(this).attr("value");
				var resourceURL ="";
				var httpType = "";
				if(buttonValue == 'Save'){
					httpType = 'POST';
					JSONObject= {"diagcode":$('#dcode').val(), "desc":$('#desc').val()
					};

					jsonData = JSON.stringify(JSONObject); 

					resourceURL= rooturl  + 'addDiagnosisCode/';

				}
				else if(buttonValue == 'Update'){

					httpType = 'POST';	
					JSONObject= {"diagcode":$('#dcode').val(), "desc":$('#desc').val()
					};
					jsonData = JSON.stringify(JSONObject); 
					resourceURL= rooturl + 'updateDiagnosisCode/' + diagnosisid;

				}

				$('#diagnosiscode').each(function(){
					$(this).validate({
						rules: {
							dcode: {
								required: true
							},
							desc:{
								required: true
							}
						},
						messages: {
							dcode: "*",
							desc: "*"

						},
						submitHandler: function(form) {
							$.ajax({
								type: httpType,
								contentType: 'application/json',
								url: resourceURL,
								async:false,
								dataType: "json",  
								data: jsonData,
								success: function(data, textStatus, error){ 
									if(buttonValue == 'Save'){
										$("#errormessage").hide();
										
										$("#successmessage").html("");
										$("#diagnosiscode :input").attr("disabled", true);
										$("#successmessage").show().html("Saved Successfully.");
									}
									else if(buttonValue == 'Update'){

										$(".btnSave").attr('value', 'Save');
										$("#diagnosiscode :input").attr("disabled", true);
										$("#errormessage").hide();
										$("#successmessage").html("");
										$("#successmessage").show().html("Updated Successfully."); 
									}
								},
								error: function(jqXHR, x, errorThrown){
									renderErrorMessage(jqXHR, x, errorThrown);
								}
							});
						}
					});
				});


			}

			//for Adding ReferralSource
			if(formid == "searchreferral"){
				
				$("#successmessage").hide();
				var buttonValue= $(this).attr("value");
				var resourceURL ="";
				var httpType = "";
				if(buttonValue == 'Save'){
					
					httpType = 'POST';
					JSONObject= {"referralsource":$('#refsource').val()
					};
					jsonData = JSON.stringify(JSONObject); 
					resourceURL= rooturl  + 'addReferralSource/';
				}
				else if(buttonValue == 'Update'){
					
					httpType = 'POST';	
					JSONObject= {"referralsource":$('#refsource').val()
					};
					jsonData = JSON.stringify(JSONObject); 
					resourceURL= rooturl + 'updateReferralSource/' + refid;
				}

				$('#referral').each(function(){
					$(this).validate({
						rules: {
							refsource: {
								required: true
							}

						},
						messages: {
							refsource: "* Required"						
						},
						submitHandler: function(form) {
							
							$.ajax({
								type: httpType,
								contentType: 'application/json',
								url: resourceURL,
								async:false,
								dataType: "json",  
								data: jsonData,
								success: function(data, textStatus, error){ 
									
									if(buttonValue == 'Save'){
										$("#errormessage").hide();
										
										$("#successmessage").html("");
										$("#referral :input").attr("disabled", true);
										$("#successmessage").show().html("Saved Successfully.");
									}
									else if(buttonValue == 'Update'){
										$(".btnSave").attr('value', 'Save');
										$("#referral :input").attr("disabled", true);
										$("#errormessage").hide();
										
										$("#successmessage").html("");
										$("#successmessage").show().html("Updated Successfully."); 
									}
								},
								error: function(jqXHR, x, errorThrown){
									renderErrorMessage(jqXHR, x, errorThrown);
								}



							});
						}
					});
				});


			}
			
			if(formid == "overriderate"){

				$("#successmessage").hide();
				var buttonValue= $(this).attr("value");
				var resourceURL ="";
				var httpType = "";
				var activityid = [];
				$("select[name*='acttypeid']").each(function(){
					var val = $(this).val();
					if(val !== '') activityid.push(val);  
				});

				var waive= [];
				$("input[name*='waive']").each(function(){
					var val = $(this).val();
					if(val !== '') waive.push(val);  
				});
				
				var therapistrate= [];
				$("input[name*='docamt']").each(function(){
					var val = $(this).val();
					if(val !== '') therapistrate.push(val);  
				});
				var netch= [];
				$("input[name*='netcharge']").each(function(){
					var val = $(this).val();
					if(val !== '') netch.push(val);  
				});
				var clientid = sessionStorage.getItem("cclientid");
				var therapistid = sessionStorage.getItem("ctherapistid");
				
				if(buttonValue == 'Save'){
					
					httpType = 'POST';
					JSONObject= {"therapistid":therapistid,"clientid":clientid,"activityid":activityid,"waiver":waive, "netch":netch
					};
					jsonData = JSON.stringify(JSONObject); 
					resourceURL= rooturl  + 'addWaiver/';
				}
				else if(buttonValue == 'Update'){
					
					httpType = 'POST';	
					JSONObject= {"therapistid":therapistid,"clientid":clientid,"activityid":activityid,"waiver":waive,"netch":netch
					};
					jsonData = JSON.stringify(JSONObject); 
					resourceURL= rooturl + 'updateWaiver/' ;
				}

				$('#overriderate').each(function(){
					$(this).validate({
						rules: {
							"waive[]": {
								required: true
							}

						},
						messages: {
							"waive[]": "* Required"						
						},
						submitHandler: function(form) {
							
							$.ajax({
								type: httpType,
								contentType: 'application/json',
								url: resourceURL,
								async:false,
								dataType: "json",  
								data: jsonData,
								success: function(data, textStatus, error){ 
									
									if(buttonValue == 'Save'){
										$("#errormessage").hide();
										
										$("#successmessage").html("");
										$("#overriderate :input").attr("disabled", true);
										$("#successmessage").show().html("Saved Successfully");
									}
									else if(buttonValue == 'Update'){
										$(".btnSave").attr('value', 'Save');
										$("#overriderate :input").attr("disabled", true);
										$("#errormessage").hide();
										
										$("#successmessage").html("");
										$("#successmessage").show().html("Updated Successfully"); 
									}
								},
								error: function(jqXHR, x, errorThrown){
									renderErrorMessage(jqXHR, x, errorThrown);
								}



							});
						}
					});
				});




			}
			if(formid == 'payments'){
				
				var cnameid = sessionStorage.getItem("cnameid");
				
				 JSONObject= {"camount":parseFloat($('#bwpayment').val(),10).toFixed(2), 
						 		"paymentdate":$('#paydate').val(),
						 		"therapistid":$('#therapistName').val(),
						 		"nameid":cnameid,
						 		"activitytype":$('#cactivity').val()
				};
				jsonData = JSON.stringify(JSONObject); 
			
				
				$('#payments').validate({
					rules: {
						cactivity:"required",
						therapistName: "required",
						bwpayment: {
							required: true,
							number: true
						},
						paydate:{
							required: true,
							date : true
						}
						
					},
					messages: {
						bwpayment:{
							required: "*",
							number: "Please enter a valid amount"
						},
						paydate: "*",
						cactivity: "*",
						therapistName: "*"
					},
					submitHandler: function(form) {
						
						
						$.ajax({
							type: 'POST',
							contentType: 'application/json',
							url: rooturl + "addPayment/",
							async:false,
							dataType: "json",  
							data: jsonData,
							success: function(data, textStatus, error){ 
								$("#errormessage").hide();
							
									$("#successmessage").html("");
									$("#payments :input").attr("disabled", true);
									$("#successmessage").show().html("Saved Successfully.");
								
							},
							error: function(jqXHR, x, errorThrown){
								renderErrorMessage(jqXHR, x, errorThrown);
							}
						});
					}
				});
				
			}
			
			

			// return false;
		});
		

		
		
		$('.btnFetch').click(function(e){
			$("#successmessage").hide();
			$("#errormessage").hide().html("");
			$('.reportList').empty();
			$('.totalNetch').empty();
			$('.totalPaid').empty();
			$('.totalBal').empty();	
		
			
			
			$('#reportym').validate({
				rules: {
					fromdate:{
						required: true,
						date : true
					},
					todate:{
						required: true,
						date : true
					},
					selectClient:{
						required: true
					}
					
					
					
				},
				messages: {
					fromdate: "*",
					
						todate:{
							required: "*"
						},
						selectClient:"* Required Field"
						
				},
				submitHandler: function(form) {
					var startDt=$('#fromdate').val();
					
					var endDt=$('#todate').val();
				

					if(startDt < endDt){
						
						var stdt = startDt.split("-");
						var stdtyear= stdt[0];
						var edt = endDt.split("-");
						var edtyear= edt[0];
						if(stdtyear >= 2016 && edtyear >= 2016){
							if($('#therapistName').val() == '')
								therapist = 0;
							else
								therapist =$('#therapistName').val();
							console.log("fromdate:"+startDt+"todate:"+endDt+"therapistid:"+therapist+"nameid:"+$('#selectClient').val());
							JSONObject= {"fromdate":startDt, "todate":endDt,"therapistid":therapist,"nameid":$('#selectClient').val()};
							jsonData = JSON.stringify(JSONObject);     
							
							$.ajax({
								type: 'POST',
								contentType: 'application/json',
								url: rooturl + "fetchreport",
								async:false,
								dataType: "json", 
								data: jsonData,
								success: function(data, textStatus, error){ 
								
									 renderReport(data,textStatus,error);
									//$('.btnAdd').attr('disabled', false);
								},
								error: function(jqXHR, x, errorThrown){
									renderErrorMessage(jqXHR, x, errorThrown);
									$("#successmessage").hide();
									$("#errormessage").show().html("");
									$("#errormessage").show().html("Error:"+errorThrown);
								}
							});
							
						}
						else{
							$("#successmessage").hide();
							$("#errormessage").show().html("");
							$("#errormessage").show().html("Reports can be generated only for the year 2016 or after. Please enter the correct dates.");
					
						}
						
					}
					else{
						$("#successmessage").hide();
						$("#errormessage").show().html("");
						$("#errormessage").show().html("Please enter a From date lesser than To date");
					}
					
					
				}
			});
			
		});
		
		function renderReportCL(data,textStatus,error){

			var list = data == null ? [] : (data instanceof Array ? data : [data]);
			$('#fullReportFrame').empty();
			if(list.length == 0){
				$('#fullReportFrame').html('<font color=red>No Results Found</font>');
			}
			
			var getDate= new Date();
			var currentDate= moment(getDate).format('MM/DD/YYYY');
			
			$('#therapistdetail').append("Client List Report<br>");
			$('#therapistdetail').append('<br>');
			$('#therapistdetail').append("For the Therapist - "+data[0].therapistname);
			$('#therapistdetail').append('<br>');
			
			
			$('#divclient').append("Date:"+currentDate+'<br>');
			
			$('#reportGenSummary').append('<div id="reportGenCol">CLIENT NAME</div><div id="reportGenCol">MOBILE</div><br>');
			
			$.each(list, function(index, reportCL) {
								
				$('#reportGenList').append('<div id="reportGenCol">'+reportCL.clientname+'</div><div id="reportGenCol">'+reportCL.mobile+'</div></br>');
				
				
			});
			
			
			$('#clientdiagnosiscd').hide();
			$('#reportTotalIns').hide();
			$('#generate').hide();
			$('.fullReportFrame').show();
			
			$('#BIprint').show();
			
		}
		
		function renderReportTOT(data,textStatus,error){
			
			
			var list = data == null ? [] : (data instanceof Array ? data : [data]);
			$('#fullReportFrame').empty();
			if(list.length == 0){
				$('#fullReportFrame').html('<font color=red>No Results Found</font>');
			}
			
			var getDate= new Date();
			var currentDate= moment(getDate).format('MM/DD/YYYY');
			var fromDate = moment(data[0].fromdate).format('MM/DD/YYYY');
			var toDate = moment(data[0].todate).format('MM/DD/YYYY');
			
			$('#therapistdetail').append("Report - Totals<br>");
			$('#therapistdetail').append('<br>');
			$('#therapistdetail').append("From the date    "+fromDate+"    to    "+toDate+"    on all accounts");
			$('#therapistdetail').append('<br>');
			$('#therapistdetail').append('<br>');
			$('#therapistdetail').append('<br>');
						
			$('#divclient').append("Date:"+currentDate+'<br>');
			
			$('#reportGenSummary').append('<div id="reportCol">CLIENT NAME</div><div id="reportTotAmt">PRIOR BALANCE</div><div id="reportTotAmt">NET CHARGE</div><div id="reportTotAmt">WAIVED</div><div id="reportTotAmt">PAYMENT</div><div id="reportTotAmt">BALANCE DUE</div>');
			$.each(list, function(index, reportTOT) {
				
						
				$('#reportGenList').append('<div id="reportCol">'+reportTOT.clientname+'</div><div id="reportTotAmt">'+reportTOT.priorBalance+'</div><div id="reportTotAmt">'+reportTOT.netcharge+'</div><div id="reportTotAmt">'+reportTOT.netwaiver+'</div><div id="reportTotAmt">'+reportTOT.netpayment+'</div><div id="reportTotAmt">'+reportTOT.balance+'</div></br>');
				
				
			});
			
			$('#reportTotalIns').hide();
			$('#generate').hide();
			$('#clientdiagnosiscd').hide();
			$('.fullReportFrame').show();
			
			$('#BIprint').show();
		 }
		
		function renderReportTM(data,textStatus,error){
			var totalNC = 0;
			var totalWaiver=0;
			var totalPay=0;
			
			var list = data == null ? [] : (data instanceof Array ? data : [data]);
			$('#fullReportFrame').empty();
			if(list.length == 0){
				$('#fullReportFrame').html('<font color=red>No Results Found</font>');
			}
			
			var getDate= new Date();
			var currentDate= moment(getDate).format('MM/DD/YYYY');
			var fromDate = moment(data[0].fromdate).format('MM/DD/YYYY');
			var toDate = moment(data[0].todate).format('MM/DD/YYYY');
			
			$('#therapistdetail').append("Money By Therapist<br>");
			$('#therapistdetail').append('<br>');
			$('#therapistdetail').append("From the date    "+fromDate+"    to    "+toDate+"    on all accounts");
			$('#therapistdetail').append('<br>');
			$('#therapistdetail').append('<br>');
			$('#therapistdetail').append('<br>');
			$('#divleft').append("TherapistName: "+data[0].therapistname+'<br>');
			
			
			
			$('#divclient').append("Date:"+currentDate+'<br>');
			
			$('#reportGenSummary').append('<div id="reportCol">CLIENT NAME</div><div id="reportAmt">NETCHARGE</div><div id="reportAmt">WAIVED</div><div id="reportAmt">PAYMENT</div>');
			$.each(list, function(index, reportTM) {
				
						if(reportTM.netcharge !=0)
							totalNC= (parseFloat(totalNC) + parseFloat(reportTM.netcharge)).toFixed(2); 
						
						if(reportTM.netwaiver != 0)
							totalWaiver = (parseFloat(totalWaiver) + parseFloat(reportTM.netwaiver)).toFixed(2);
						
						if(reportTM.netpayment != 0)
							totalPay = (parseFloat(totalPay) + parseFloat(reportTM.netpayment)).toFixed(2);
					
				$('#reportGenList').append('<div id="reportCol">'+reportTM.clientname+'</div><div id="reportAmt">'+reportTM.netcharge+'</div><div id="reportAmt">'+reportTM.netwaiver+'</div><div id="reportAmt">'+reportTM.netpayment+'</div></br>');
				
				
			});
			
			$('#reportTotalIns').append('<div id="reportCol">Total</div><div id="reportAmt"><b>'+totalNC+'</b></div><div id="reportAmt"><b>'+totalWaiver+'</b></div><div id="reportAmt"><b>'+totalPay+'</b></div></br>');
			$('#generate').hide();
			$('#clientdiagnosiscd').hide();
			$('.fullReportFrame').show();
			
			$('#BIprint').show();
		 }
		
		function renderReportSummary(data,textStatus,error){
			var list = data == null ? [] : (data instanceof Array ? data : [data]);
			$('#fullReportFrame').empty();
			if(list.length == 0){
				$('#fullReportFrame').html('<font color=red>No Results Found</font>');
			}
			console.log("Clientaddress:"+data[0].caddress1);
			$('#therapistdetail').append(data[0].therapistname+'<br>');
			$('#therapistdetail').append(data[0].address1 + "," +data[0].address2+'<br>');
			$('#therapistdetail').append(data[0].city + "," +data[0].state +"-"+data[0].zipcode+'<br><br>');
			$('#therapistdetail').append("Mobile: "+data[0].mobile +'<br><br>');
			
			$('#divleft').append('<br>'+data[0].clientname+'<br>');
			$('#divleft').append(data[0].caddress1+" ,"+data[0].caddress2+'<br>');
			$('#divleft').append(data[0].ccity+","+data[0].cstate+"-"+data[0].czipcode+'<br>');
			var getDate= new Date();
			var currentDate= moment(getDate).format('MM/DD/YYYY');
			$('#divclient').append("Date:"+currentDate+'<br>');
			$('#divclient').append('<br>');$('#divclient').append('<br>');
			
			$('#generate').hide();
			var totalNC =0;
			var totalPay =0;
			var totalBal =0;
			/*$("#reportGenCol").width('18%');
			$("#reportGenAmt").width('18%');*/
			$('#reportGenSummary').append('<div id="reportCol">DATE</div><div id="reportCol">ACTIVITY NAME</div><div id="reportAmt">NetCharge</div><div id="reportAmt">Payment</div><div id="reportAmt">Balance</div>');
			$.each(list, function(index, reportsum) {
				
				if(index!=0 ){
					var activityDate = moment(reportsum.activityDate).format('MM/DD/YYYY');
					totalNC= (parseFloat(totalNC) + parseFloat(reportsum.netcharge)).toFixed(2); 
					totalPay = (parseFloat(totalPay) + parseFloat(reportsum.payment)).toFixed(2);
					totalBal = (parseFloat(totalBal) + parseFloat(reportsum.balance)).toFixed(2);
					
				$('#reportGenList').append('<div id="reportCol">'+activityDate+'</div><div id="reportCol">'+reportsum.activityname+'</div><div id="reportAmt">'+reportsum.netcharge+'</div><div id="reportAmt">'+reportsum.payment+'</div><div id="reportAmt">'+reportsum.balance+'</div></br>');
				}
				
			});
			
			
			$('#reportTotalIns').append('<div id="reportCol">'+" "+'</div><div id="reportCol">TOTAL</div><div id="reportAmt"><b>'+totalNC+'</b></div><div id="reportAmt"><b>'+totalPay+'</b></div><div id="reportAmt"><b>'+totalBal+'</b></div></br>');
			$('.fullReportFrame').show();
			$('#BIprint').show();
			/*$(".rightmiddleFrame").show().load("Testreport.html", function() {
		        $("#billsummary").show();
		    });*/ 
			

		 }
		
		function renderReportInsurance(data,textStatus,error){
			var list = data == null ? [] : (data instanceof Array ? data : [data]);
			$('#fullReportFrame').empty();
			if(list.length == 0){
				$('#fullReportFrame').html('<font color=red>No Results Found</font>');
			}
			console.log("Clientaddress:"+data[0].caddress1);
			$('#therapistdetail').append(data[0].therapistname+'<br>');
			$('#therapistdetail').append(data[0].address1 + "," +data[0].address2+'<br>');
			$('#therapistdetail').append(data[0].city + "," +data[0].state +"-"+data[0].zipcode+'<br><br>');
			$('#divleft').append("EIN    : "+data[0].ein+'<br>');
			$('#divleft').append("LICENSE: "+data[0].license+'<br>');
			$('#divright').append("MOBILE: "+data[0].mobile+'<br>');
			$('#divleft').append('<br>'+data[0].clientname+'<br>');
			$('#divleft').append(data[0].caddress1+" ,"+data[0].caddress2+'<br>');
			$('#divleft').append(data[0].ccity+","+data[0].cstate+"-"+data[0].czipcode+'<br>');
			var getDate= new Date();
			var currentDate= moment(getDate).format('MM/DD/YYYY');
			$('#divclient').append("Date:"+currentDate+'<br>');
			$('#divclient').append('<br>');$('#divclient').append('<br>');
			
			$('#clientdiagnosiscd').append("Diagnosis Code:"+data[0].diagnosiscode);
			/*$('.fullReportFrame').append($('#therapistdetail'));
			$('.fullReportFrame').append($('#divright'));
			$('.fullReportFrame').append($('#divleft'));
			$('.fullReportFrame').append($('#divclient'));*/
			
			$('#generate').hide();
			var total =0;
			
			$('#reportGenSummary').append('<div id="reportGenCol">ActivityDate</div><div id="reportGenCol">CPTCode</div><div id="reportGenAmt">NetCharge</div>');
			$.each(list, function(index, reportsum) {
				console.log("value of index"+index);
				
				if(index!=0 ){
					var activityDate = moment(reportsum.activityDate).format('MM/DD/YYYY');
					total= total + reportsum.netcharge; 
				$('#reportGenList').append('<div id="reportGenCol">'+activityDate+'</div><div id="reportGenAmt">'+reportsum.cptcode+'</div><div id="reportGenAmt">'+reportsum.netcharge+'</div></br>');
				}
				
			});
			
			
			$('#reportTotalIns').append('<div id="reportGenCol">'+" "+'</div><div id="reportGenAmt">TOTAL</div><div id="reportGenAmt"><b>'+total+'</b></div></br>');
			$('.fullReportFrame').show();
			$('#BIprint').show();
			/*$(".rightmiddleFrame").show().load("Testreport.html", function() {
		        $("#billsummary").show();
		    });*/ 
			

		 }
		
		function renderReport(data,textStatus,error){
			var list = data == null ? [] : (data instanceof Array ? data : [data]);
			$('#reportList').empty();
			$('#reportTotalIns').empty();
			
			if(list.length == 0){
				$('#reportList').html('<font color=red>No Results Found</font>');
			}
			
			var tnch =0;
			var tpay =0;
			var tbal =0;
			$.each(list, function(index, report) {
				
				var nchval = parseFloat(report.netcharge).toFixed(2);
				var payval = parseFloat(report.payment).toFixed(2);
				var balval= parseFloat(report.balance).toFixed(2);
				tnch = (parseFloat(nchval) + parseFloat(tnch)).toFixed(2);
				tpay = (parseFloat(payval) + parseFloat(tpay)).toFixed(2);
				tbal = (parseFloat(balval) + parseFloat(tbal)).toFixed(2);
				
				var responseDate = moment(report.activitydate).format('MM/DD/YYYY');
				
				$('#reportList').append('<div id="reportCol">'+responseDate+'</div><div id="reportCol">'+report.activity+'</div><div id="reportAmt">'+report.netcharge+'</div><div id="reportAmt">'+report.payment+'</div><div id="reportAmt">'+report.balance+'</div>');

			});
			$('#reportTotalIns').append('<div id="reportCol">'+" "+'</div><div id="reportCol">TOTAL</div><div id="reportAmt"><b>'+tnch+'</b></div><div id="reportAmt"><b>'+tpay+'</b></div><div id="reportAmt"><b>'+tbal+'</b></div></br>');
			/*$('#totalNetch').append(tnch);
			$('#totalPaid').append(tpay);
			$('#totalBal').append(tbal);*/
			$('.fullReportFrame').show();
		
		
		 }
		
		function Popup(data) 
		{
		    var mywindow =  window.open('', '.fullReportFrame', 'height=400,width=600');
		   
		    mywindow.document.write('<html><head><title></title>');
		    
		    mywindow.document.write(' <link rel="stylesheet" href="../css/common.css"  type="text/css" />');  
		    mywindow.document.write('</head><body>');
		    mywindow.document.write(data);
		   
		    mywindow.document.write('</body></html>');
		    mywindow.document.close(); // necessary for IE >= 10

		   var myDelay = setInterval(checkReadyState, 10);

		    function checkReadyState() {
		        if (mywindow.document.readyState == "complete") {
		            clearInterval(myDelay);
		            mywindow.focus(); // necessary for IE >= 10

		            mywindow.print();
		           // mywindow.close();
		        }
		    }

		    return true;
		}


		$('.btnPrint').click(function(e){
			var html=$(".fullReportFrame").html();
		
			//$(".fullReportFrame").printElement();
			Popup(html);
			
			 /*var mywindow = window.open('', '.fullReportFrame', 'height=400,width=600');
			    mywindow.document.write('<html><head><title></title>');
			   
			    mywindow.document.write(' <link rel="stylesheet" href="../css/common.css">');  
			    mywindow.document.write('</head><body>');
			    mywindow.document.write(html);
			    mywindow.document.write('</body></html>');
			    mywindow.document.close();
			    
			    mywindow.print();  
			    setTimeout(function(){mywindow.close();}, 1);
			    setTimeout(function () {
			        //mywindow.print();
			        mywindow.close();
			    }, 500); */
	
		});
		
		//Update Values

		$('.btnModify').click(function(e){

			$('#addFlag').validate({
				rules: {
					newflag: {
						required: true
					}
				},
				messages: {
					newflag: "* Required Field"
				},
				submitHandler: function(form) {

					var updflagname = $("#newflag").val();
					var flagsearchval = $("#flagsearchtext").val();
					e.preventDefault();
					JSONObject= {"oldflagname":flagsearchval, "flagname":updflagname};
					jsonData = JSON.stringify(JSONObject);     

					$.ajax({
						type: 'POST',
						contentType: 'application/json',
						url: rooturl,
						async:false,
						dataType: "json",   
						data: jsonData,
						success: function(data, textStatus, error){ 
							$("#errormessage").hide();
							$("#successmessage").show().html('');
							$("#successmessage").show().html("Updated Successfully"); 
							$('.btnAdd').attr('disabled', false);
						},
						error: function(jqXHR, x, errorThrown){
							renderErrorMessage(jqXHR, x, errorThrown);
						}
					});
				}
			});

		});
		
		/*$('.btnNewAcivity').click(function(e){
			alert("clicked");
			 $(".rightmiddleFrame").show().load("newactivity.html"); 
			
		});*/
	/*	$("ul#clientList li").on('click', '.btnNewAcivity', function(e){
			alert("buttonclicked");
		});*/
		/*$("ul#clientList > li#testactivity ").one().click(function(e){
			alert("buttonclicked");
		});*/
		
		//Delete Values

		$('.btnDelete').click(function(e){
			
			if(formid == 'flagSearch'){

			if(jQuery.trim($('#flagsearchtext').val()).length > 0)
			{
				if(flagid.length  > 0)
				{
					$.ajax({
						type: 'DELETE',
						contentType: 'application/json',
						url: rooturl  + 'deleteFlag/' + flagid,
						async:false,
						dataType: "json",    				      				     
						success: function(data, textStatus, error){  
							
							$("flagsearchtext").val('');
							$("#errormessage").hide();
							
							$("#successmessage").show().html("");
							$("#successmessage").show().html("Flag Deleted"); 
							//$('.btnAdd').attr('disabled', false);
						},
						error: function(jqXHR, x, errorThrown){
							renderErrorMessage(jqXHR, x, errorThrown);
							$("#successmessage").hide();
							$("#errormessage").show().html("");
							$("#errormessage").show().html("Error:"+message);
						}
					});
				}
				else{
					e.preventDefault();
					$("#successmessage").hide();
					$("#errormessage").show().html("");
					$("#errormessage").show().html("Please Select a Value from Search List");  
					// $("#flagSearch").unbind('submit').submit();

				}
			}else{
				e.preventDefault();
				$("#successmessage").hide();
				$("#errormessage").show().html("");
				
				$("#errormessage").show().html("Please Enter a Flag Value");
				// $("#flagSearch").unbind('submit').submit();

			}
			}
			else if(formid == 'searchactivity'){
				if(jQuery.trim($('#searchactivitytype').val()).length > 0)
				{
					
					if (actTypeid != null	)
					
					{
						$.ajax({
							type: 'DELETE',
							contentType: 'application/json',
							url: rooturl  + 'deleteActivityType/' + actTypeid,
							async:false,
							dataType: "json",    				      				     
							success: function(data, textStatus, error){  
								$("#errormessage").hide();
								$("#successmessage").show().html('');
								$("flagsearchtext").val('');
								$("#successmessage").show().html("ActivityType Deleted"); 
								//$('.btnAdd').attr('disabled', false);
							},
							error: function(jqXHR, x, errorThrown){
								renderErrorMessage(jqXHR, x, errorThrown);
								$("#successmessage").hide();
								$("#errormessage").show().html("");
								$("#errormessage").show().html("Error:"+message);
							}
						});
					}
					else{
						e.preventDefault();
						$("#successmessage").hide();
						$("#errormessage").show().html("");
						$("#errormessage").show().html("Please Select a Value from Search List");  
						// $("#flagSearch").unbind('submit').submit();

					}
				}else{
					e.preventDefault();
					$("#successmessage").hide();
					$("#errormessage").show().html("");
					$("#errormessage").show().html("Please Enter a Activity Type Value");
					// $("#flagSearch").unbind('submit').submit();

				}
				
			}
			else if(formid == 'searchdiagnosiscode'){
				
			if(jQuery.trim($('#searchdiagcode').val()).length > 0)
			{
				
				if (diagnosisid != null && diagnosisid != "")
				{
					$.ajax({
						type: 'DELETE',
						contentType: 'application/json',
						url: rooturl  + 'deleteDiagnosisCode/' + diagnosisid,
						async:false,
						dataType: "json",    				      				     
						success: function(data, textStatus, error){  
							$("#errormessage").hide();
							
							$("#successmessage").html(""); 
							$("#successmessage").show().html("DiagnosisCode Deleted"); 
							$("#diagnosiscode :input").attr("disabled", true);
						},
						error: function(jqXHR, x, errorThrown){
							renderErrorMessage(jqXHR, x, errorThrown);
							$("#errormessage").html("");
							$("#errormessage").show().html("Error:"+message);
						}
					});
				}
				else{
					e.preventDefault();
					$("#successmessage").hide();
					$("#errormessage").show().html("");
					$("#errormessage").show().html("Please Select a Value from Search List");  
					// $("#flagSearch").unbind('submit').submit();

				}
			}else{
				e.preventDefault();
				$("#successmessage").hide();
				$("#errormessage").show().html("");
				$("#errormessage").show().html("Please Enter a Diagnosis Code value");
				// $("#flagSearch").unbind('submit').submit();

			}
			}
		
			else if(formid == 'doctor'){
				var delnameid = sessionStorage.getItem("nameid");
				$.ajax({
					type: 'DELETE',
					contentType: 'application/json',
					url: rooturl  + 'deleteTherapist/' + delnameid,
					async:false,
					dataType: "json",    				      				     
					success: function(data, textStatus, error){  
						$("#doctor :input").attr("disabled", true);
						$("#errormessage").hide();
						$("#successmessage").show().html('');
						$("#successmessage").show().html("Therapist Deleted"); 
						//$('.btnAdd').attr('disabled', false);
					},
					error: function(jqXHR, x, errorThrown){
						renderErrorMessage(jqXHR, x, errorThrown);
						$("#successmessage").hide();
						$("#errormessage").show().html("");
						$("#errormessage").show().html("Error:"+message);
					}
				});
				
			}
			else if(formid == 'client'){
				var delclientid = sessionStorage.getItem("cclientid");
				
				$.ajax({
					type: 'DELETE',
					contentType: 'application/json',
					url: rooturl  + 'deleteClient/' + delclientid,
					async:false,
					dataType: "json",    				      				     
					success: function(data, textStatus, error){  
						$("#client :input").attr("disabled", true);
						$("#errormessage").hide();
						$("#successmessage").show().html('');
						$("#successmessage").show().html("Client Deleted"); 
						
					},
					error: function(jqXHR, x, errorThrown){
						renderErrorMessage(jqXHR, x, errorThrown);
						
					}
				});
				
			}
			else if(formid == "searchreferral"){
				

				if(jQuery.trim($('#searchrefsource').val()).length > 0)
				{
					if(refid != '' && refid != 'undefined' && refid != null)
					{
						$.ajax({
							type: 'DELETE',
							contentType: 'application/json',
							url: rooturl  + 'deleteReferral/' + refid,
							async:false,
							dataType: "json",    				      				     
							success: function(data, textStatus, error){  
								
								$("searchrefsource").val('');
								$("#errormessage").hide();
								
								$("#successmessage").show().html("");
								$("#successmessage").show().html("Referral Source Deleted"); 
								//$('.btnAdd').attr('disabled', false);
							},
							error: function(jqXHR, x, errorThrown){
								renderErrorMessage(jqXHR, x, errorThrown);
								//$("#searchmessage").show().html("Error:"+message);
							}
						});
					}
					else{
						e.preventDefault();
						$("#successmessage").hide();
						$("#errormessage").show().html("");
						$("#errormessage").show().html("Please Select a Value from Search List");  
						// $("#flagSearch").unbind('submit').submit();

					}
				}else{
					e.preventDefault();
					$("#successmessage").hide();
					$("#errormessage").show().html("");
					$("#errormessage").show().html("Please Enter a Referral Source");
					// $("#flagSearch").unbind('submit').submit();

				}
				
			}
			else if(formid == "newactivity"){

				alert("in new activity");
				
					var newactivityPresent = sessionStorage.getItem("newactivity");
					alert(newactivityPresent);
					if(newactivityPresent != '' && newactivityPresent != 'undefined' && newactivityPresent != null)
					{
						var cnameid = sessionStorage.getItem("cnameid");
						var tid = $('#therapistName').val();
						
						JSONObject= {	
								"nameid":			cnameid,
								"cname":			$('#cname').val(), 
								"therapistid":		tid,
								"acttypeid":		$('#cactivity').val(), 
								"rateperval":   	$('#rateperval').val(),
								"rateper":			$('#report').val(),
								"newactwaiver":		parseFloat($('#waiver').val(),10).toFixed(2),
								"newactnetcharge":	parseFloat($('#netch').val(),10).toFixed(2),
								"camount":			parseFloat($('#rate').val(),10).toFixed(2),
								"payment":			parseFloat($('#payment').val(),10).toFixed(2),
								"paymentdate":		$('#actydate').val()
							};
						jsonData = JSON.stringify(JSONObject); 
						alert(jsonData);
						$.ajax({
							type: 'DELETE',
							contentType: 'application/json',
							url: rooturl  + 'deleteNewActivity/' ,
							async:false,
							dataType: "json",    
							data: jsonData,
							cache: false,							
							success: function(data, textStatus, error){  
								
								
								$("#errormessage").hide();
								
								$("#successmessage").show().html("");
								$("#successmessage").show().html("Activity Deleted"); 
								$('.btnAdd').attr('disabled', true);
							},
							error: function(jqXHR, x, errorThrown){
								renderErrorMessage(jqXHR, x, errorThrown);
								//$("#searchmessage").show().html("Error:"+message);
							}
						});
					}
					else{
						e.preventDefault();
						$("#successmessage").hide();
						$("#errormessage").show().html("");
						$("#errormessage").show().html("Please Select a Value from Search List");  
						// $("#flagSearch").unbind('submit').submit();

					}
				
				
			
				
				
			}
			


		});

		//Search Values

		$('.btnSearch').click(function(e){
			
			$("#successmessage").hide();  
			$("#errormessage").hide(); 
			
			if(formid == 'flagSearch'){
				$('#flagList').html('');
				$('#flagSearch').each(function(){
					$(this).validate({
						rules: {
							flagsearchtext: {
								required: true
							}
						},
						messages: {
							flagsearchtext: "* Required Field"

						},
						submitHandler: function(form) {

							$.ajax({
								type: 'GET',
								contentType: 'application/json',
								url: rooturl  + 'searchFlag/' + $('#flagsearchtext').val(),
								async:false,
								dataType: "json",    				      				     
								success: function(data, textStatus, error){  
									renderFlagList(data);
								},
								error: function(jqXHR, x, errorThrown){
									renderErrorMessage(jqXHR, x, errorThrown);
								}


							});
						}
					});
				});
			}//Search Activity Type
			else if(formid == 'searchactivity'){
				$('#searchActTypeList ').html('');
				$('#searchactivity').validate({
					rules: {
						searchactivitytype: {
							required: true
						}
					},
					messages: {
						searchactivitytype: "* Required"
					},
					submitHandler: function(form) {
					
						$.ajax({
							type: 'GET',
							contentType: 'application/json',
							url: rooturl  + 'searchActivityType/' + $('#searchactivitytype').val(),							
							async:false,
							dataType: "json", 
							
							success: function(data, textStatus, error){  
								renderActTypeList(data);
								
							},
							error: function(jqXHR, x, errorThrown){
								renderErrorMessage(jqXHR, x, errorThrown);
							}


						});
					}
				});
			}
			else if(formid == 'searchTherapist'){
				$('#therapistList').html('');
			
				$('#searchTherapist').each(function(){
					$(this).validate({
						rules: {
							tsearchfirstname: {
								required: true
							},
							tsearchlastname:{
								required: true
							}
						},
						messages: {
							tsearchfirstname: "*",
							tsearchlastname: "*"

						},
						submitHandler: function(form) {

							$.ajax({
								type: 'GET',
								contentType: 'application/json',
								url: rooturl  + 'searchTherapist/' + $('#tsearchfirstname').val() + "/" + $('#tsearchlastname').val(),
								async:false,
								dataType: "json",    				      				     
								success: function(data, textStatus, error){  
									
									renderTherapistList(data);
								},
								error: function(jqXHR, x, errorThrown){
									renderErrorMessage(jqXHR, x, errorThrown);
								}


							});
						}
					});
				});
			}
		
			//search Diagnosis Code
			else if(formid == 'searchdiagnosiscode'){
				$('#searchDiagnosisCodeList').html('');
				
				$('#searchdiagnosiscode').validate({
					rules: {
						searchdiagcode: {
							required: true
						}
					},
					messages: {
						searchdiagcode: "* Required"
					},
					submitHandler: function(form) {
					
						$.ajax({
							type: 'GET',
							contentType: 'application/json',
							url: rooturl  + 'searchDiagnosisCode/' + $('#searchdiagcode').val(),							
							async:false,
							dataType: "json", 
							
							success: function(data, textStatus, error){  
								renderDiagnosisCodeList(data);
								
							},
							error: function(jqXHR, x, errorThrown){
								renderErrorMessage(jqXHR, x, errorThrown);
							}


						});
					}
				});
			}
			else if(formid == 'searchreferral'){
					
				$('#searchReferralList').html('');
				$('#searchreferral').validate({
					rules: {
						searchrefsource: {
							required: true
						}
					},
					messages: {
						searchrefsource: "* Required"
					},
					submitHandler: function(form) {
					
						$.ajax({
							type: 'GET',
							contentType: 'application/json',
							url: rooturl  + 'searchReferralSource/' + $('#searchrefsource').val(),							
							async:false,
							dataType: "json", 
							
							success: function(data, textStatus, error){  
								renderReferralSourceList(data);
								
							},
							error: function(jqXHR, x, errorThrown){
								renderErrorMessage(jqXHR, x, errorThrown);
							}


						});
					}
				});
			
			}
			else if(formid == 'newactivity'){
				
				$('#searchNewActTypeList').html('');
				$('#searchnewactivity').validate({
					rules: {
						csearchactivity: {
							required: true
						},
						searchtherapistName:{
							required: true
						},
						selectClient:{
							required: true
						},
						
					},
					messages: {
						csearchactivity: "*",
						searchtherapistName: "*",
						selectClient: "* Required"
					},
					submitHandler: function(form) {
						JSONObject= {"acttypeid":$('#csearchactivity').val(), "therapistid":$('#searchtherapistName').val(),"nameid":$('#selectClient').val()};
						jsonData = JSON.stringify(JSONObject); 
						
						$.ajax({
							type: 'POST',
							contentType: 'application/json',
							url: rooturl  + 'searchNewActivity/' ,							
							async:false,
							dataType: "json", 
							data: jsonData,
							cache: false,
							success: function(data, textStatus, error){ 
							
								renderSearchNewActivity(data);
								
							},
							error: function(jqXHR, x, errorThrown){
								renderErrorMessage(jqXHR, x, errorThrown);
							}


						});
					}
				});
			
			}
		});
		
		//Search Client
		$('.btnSearchClient').click(function(e){
			$('#clientList').html('');
			if(formid == 'searchTherapist'){
				$('#searchClient').validate({
				
						rules: {
							csearchfirstname: {
								required: true
							},
							csearchlastname:{
								required: true
							}
						},
						messages: {
							csearchfirstname: "*",
							csearchlastname: "*"

						},
						submitHandler: function(form) {
							
							$.ajax({
								type: 'GET',
								contentType: 'application/json',
								url: rooturl  + 'searchClient/' + $('#csearchfirstname').val() + "/" + $('#csearchlastname').val(),
								async:false,
								dataType: "json",    				      				     
								success: function(data, textStatus, error){  
									renderClientList(data);
								},
								error: function(jqXHR, x, errorThrown){
									renderErrorMessage(jqXHR, x, errorThrown);
								}


							});
						}
					});
				
			}
			
		});



		$('#flagList').on('click','li',function(){
			$('#newflag').val($(this).text());
			flagid= $(this).attr('id');
			//$(".btnAdd").attr('disabled', false);
			$(".btnAdd").attr('value', 'Update');
			
			//$('#newflag').attr('readonly', false);
			
			

		});
		$('#therapistList').on('click','li',function(){
			
			nameid= $(this).attr('id');
			
			sessionStorage.setItem("nameid",nameid);
			findByNameId(nameid);
			
			

		});
		//To render Flag search list
		function renderFlagList(data) {
			// JAX-RS serializes an empty list as null, and a 'collection of one' as an object (not an 'array of one')
			var list = data == null ? [] : (data instanceof Array ? data : [data]);
			$('#flagList li').remove();
			if(list.length == 0){
				$('#flagList').html('<font color=red>No Results Found</font>');
			}
			$.each(list, function(index, flag) {
				$('#flagList').append('<li id="'+flag.flagid+'"><a href="#" data-identity="' + flag.flagid + '">'+flag.flagname+'</a></li>');

			});
		}
		//To render Therapist search list
		function renderTherapistList(data) {
			
			console.log(data);
			var list = data == null ? [] : (data instanceof Array ? data : [data]);
			$('#therapistList li').remove();
			$('#searchdoc').show();
			if(list.length == 0){
				$('#therapistList').html('<font color=red>No Results Found</font>');
			}
			$.each(list, function(index, therapist) {
				
				var docName =  therapist.lastname + "," + therapist.firstname + " " + therapist.middlename ;
				
				$('#therapistList').append('<li id="'+therapist.nameid+'"><a href="#" data-identity="' + therapist.nameid + '">'+docName+'</a></li>');

			});
		}
		//Render Client List
		function renderClientList(data){
			console.log(data);
			var list = data == null ? [] : (data instanceof Array ? data : [data]);
			$('#clientList li').remove();
			$('#searchclient').show();
			if(list.length == 0){
				$('#clientList').html('<font color=red>No Results Found</font>');
			}
			$.each(list, function(index, client) {
				
				var clientName =  client.lastname + "," + client.firstname + " " + client.middlename;
				var cname = client.firstname+" "+client.lastname;
				
				$('#clientList').append('<tr><td id="edit"><li id="'+client.nameid+ '-'+cname+'"><a href="#" data-identity="' + client.nameid + '">'+clientName+'</a></li></td><td id="activity"><li id="' + client.nameid + '-'+cname+'"><a href="#" id="' + client.nameid + '">New Activity</a></li></td><td id="payments"><li id="'+client.nameid+ '-'+cname+'"><a href="#" id="' + client.nameid +'"> Payments</a></li></td></tr>');

			});
		}
		
		$("#clientList ").on("click", "li", function() {
			var cattrid= $(this).attr('id');
			var cnameidval =cattrid.split("-");
			
			var cnameid = cnameidval[0];
			var cnameval = cnameidval[1];
							
			var tdid = $(this).closest('td').attr('id');	
			 sessionStorage.setItem("theader",cnameval);
			 sessionStorage.setItem("cnameid",cnameid);
			// sessionStorage.setItem("cnameval",cnameval);
			 sessionStorage.setItem("cactivityname",cnameval);
			 if(tdid == 'activity'){
				 $(".rightmiddleFrame").show().load("newactivity.html"); 
			 }
			 else if(tdid == 'edit'){
				 $(".rightmiddleFrame").show().load("client.html"); 
				 getClientDetails();
			 }
			 else if(tdid == 'payments'){
				 $(".rightmiddleFrame").show().load("clientpayment.html"); 
			 }
			
			 
		});
		// To render Activity Type List
		function renderActTypeList(data) {
			// JAX-RS serializes an empty list as null, and a 'collection of one' as an object (not an 'array of one')
			var list = data == null ? [] : (data instanceof Array ? data : [data]);
			$('#searchActTypeList li').remove();
			
			if(list.length == 0){
				$('#searchActTypeList').html('<font color=red>No Results Found</font>');
			}
			$.each(list, function(index, acttype) {
				$('#searchActTypeList').append('<li id="'+acttype.typeid+'"><a href="#" data-identity="' + acttype.typeid + '">'+acttype.activitytype+'</a></li>');
			});
		}
		// To render Diagnosis Code List
		function renderDiagnosisCodeList(data) {
			// JAX-RS serializes an empty list as null, and a 'collection of one' as an object (not an 'array of one')
			var list = data == null ? [] : (data instanceof Array ? data : [data]);
			$('#searchDiagnosisCodeList li').remove();
			
			if(list.length == 0){
				$('#searchDiagnosisCodeList').html('<font color=red>No Results Found</font>');
			}
			console.log("Diagnosis Code:"+data);
			$.each(list, function(index, diagcode) {
				$('#searchDiagnosisCodeList').append('<li id="'+diagcode.diagnosisid+'"><a href="#" data-identity="' + diagcode.diagnosisid + '">'+diagcode.diagcode+'</a></li>');
			});
		}
		//Search the Diagnosis Code list
		
		$('#searchDiagnosisCodeList').on('click','li',function(){
			$('#searchdiagcode').val($(this).text());
			diagnosisid= $(this).attr('id');
			
			findByDiagnosisId(diagnosisid);
			$(".btnSave").attr('value', 'Update');
		});
		
		//To display the selected value from the activity type search list
		function findByDiagnosisId(id) {
			console.log('findByDiagnosisId: ' + id);
			$.ajax({
				type: 'GET',
				url: rooturl + 'DiagnosisCode/' + id,
				dataType: "json",
				success: function(data){
					//$('#btnDelete').show();
					console.log('findByDiagnosisId success: ' + data.diagnosisid);
					currentType = data;
					renderDiagnosisDetails(currentType);
				}
			});
		}
		
		function renderDiagnosisDetails(code) {
			$('#dcode').val(code.diagcode);
			$('#desc').val(code.desc);
			diagnosisid = code.diagnosisid;
			
		}
		
		//To render Search New Activity
		
		function renderSearchNewActivity(data) {
			
			var list = data == null ? [] : (data instanceof Array ? data : [data]);
			$('#searchNewActTypeList li').remove();
			
			if(list.length == 0){
				$('#searchNewActTypeList').html('<font color=red>No Results Found</font>');
			}
			$.each(list, function(index, newActivity) {
				$('#searchNewActTypeList').append('<li id="'+newActivity.acttypeid+'"><a href="#" data-identity="' + newActivity.paymentid + '">'+newActivity.activitytype+ '/' +newActivity.activitydate+'</a></li><li id="clientid">'+newActivity.clientid+'</li><li id="thid">'+newActivity.therapistid+'</li><li id="pid">'+newActivity.paymentid+'</li>');
			});
		}
		
	//Search the New Activity Type list
		
		$('#searchNewActTypeList').on('click','li',function(){
				 aType = $(this).attr('id');
				 cid = $('#clientid').text();
				 tid = $('#thid').text();
				 pid = $('#pid').text();
				aDateArr = ($(this).text()).split('/');
				 aDate = aDateArr[1];
				
				findByNewActivityID(cid,tid,aDate,aType,pid);
			$(".btnSave").attr('value', 'Update');
		});
		
		//To display the selected value from the new activity search list
		function findByNewActivityID(cid,tid,aDate, aType,pid) {
			JSONObject= {"clientid":cid, "therapistid":tid,"activitydate":aDate,"acttypeid":aType,"paymentid":pid};
			jsonData = JSON.stringify(JSONObject); 
			
			$.ajax({
				type: 'POST',
				contentType: 'application/json',
				url: rooturl + 'findNewActivity/'  ,
				async:false,
				dataType: "json",
				data: jsonData,
				cache: false,
				success: function(data){
				
					currentType = data;
					renderNewActivityDetails(currentType);
				},
				error: function(jqXHR, x, errorThrown){
					renderErrorMessage(jqXHR, x, errorThrown);
				}
			});
		}
		
		function renderNewActivityDetails(type) {
			
			$('#cname').val(type.firstname +" "+type.lastname);
			$('#cactivity').val(type.acttypeid);
			var activityDate= moment(type.activitydate).format('MM/DD/YYYY');
			
			$('#actydate').val(type.activitydate);
			$('#therapistName').val(type.therapistid);
			$('#report').val(type.rateper);
			$('#rateperval').val(type.rateperval);
			$('#rate').val(type.camount);
			$('#waiver').val(type.newactwaiver);
			$('#netch').val(type.newactnetcharge);
			$('#payment').val(type.payment);
			
			sessionStorage.setItem("newactivity","yes");
			/*$('#aType').val(type.activitytype);
			$('#rate').val(type.rate);
			$('#cptcode').val(type.cptcode);
			$('#rateper').val(type.rateper);
			$('#desc').val(type.desc);
			actTypeid = type.typeid;*/
			
		}
		
		//To render Referral Source 
		
		function renderReferralSourceList(data) {
			var list = data == null ? [] : (data instanceof Array ? data : [data]);
			$('#searchReferralList li').remove();
			
			if(list.length == 0){
				$('#searchReferralList').html('<font color=red>No Results Found</font>');
			}
			$.each(list, function(index, refsource) {
				$('#searchReferralList').append('<li id="'+refsource.referralid+'"><a href="#" data-identity="' + refsource.referralid + '">'+refsource.referralsource+'</a></li>');
			});
		}
		
	
		
		//Search the Referral Source list
		
		$('#searchReferralList').on('click','li',function(){
			$('#searchrefsource').val($(this).text());
			refid= $(this).attr('id');
			
			findByReferralId(refid);
			$(".btnSave").attr('value', 'Update');
		});
		
		
		
		//To display the selected value from the activity type search list
		function findByReferralId(id) {
			console.log('findByReferralId: ' + id);
			$.ajax({
				type: 'GET',
				url: rooturl + 'ReferralSource/' + id,
				dataType: "json",
				success: function(data){
					//$('#btnDelete').show();
					console.log('findByReferralId success: ' + data.referralid);
					currentType = data;
					renderReferralDetails(currentType);
				},
				error: function(jqXHR, x, errorThrown){
					renderErrorMessage(jqXHR, x, errorThrown);
				}
			});
		}
		
		function renderReferralDetails(code) {
			$('#refsource').val(code.referralsource);
			refid = code.referralid;
			
		}
		
		
		//Search Activity Type List
		
		$('#searchActTypeList').on('click','li',function(){
			$('#searchactivitytype').val($(this).text());
			actTypeid= $(this).attr('id');
			
			findById(actTypeid);
			$(".btnAdd").attr('value', 'Update');
		});
		
		//To display the selected value from the activity type search list
		function findById(id) {
			console.log('findById: ' + id);
			$.ajax({
				type: 'GET',
				url: rooturl + 'activitytype/' + id,
				dataType: "json",
				success: function(data){
					//$('#btnDelete').show();
					console.log('findById success: ' + data.activitytype);
					currentType = data;
					renderDetails(currentType);
				}
			});
		}
		
		function renderDetails(type) {
			$('#aType').val(type.activitytype);
			$('#rate').val(type.rate);
			$('#cptcode').val(type.cptcode);
			$('#rateper').val(type.rateper);
			$('#desc').val(type.desc);
			actTypeid = type.typeid;
			
		}
		//To display the selected value from the therapist search list
		function findByNameId(id) {
			console.log('findByNameId: ' + id);
			$.ajax({
				type: 'GET',
				url: rooturl + 'therapistName/' + id,
				dataType: "json",
				success: function(data){
					//$('#btnDelete').show();
					console.log('findByNameId success: ' + data);
					 
					 renderTherapistDetails(data);
					 $(".rightmiddleFrame").show().load("doctor.html"); 
					
				}
			});
		}
		
		function renderTherapistDetails(data) {
			
			var therapistHeader =data.firstname +" "+data.lastname;
		
			console.log(data);
			sessionStorage.setItem("theader",therapistHeader);
			sessionStorage.setItem("tfirstname",data.firstname);
			sessionStorage.setItem("tlastname",data.lastname);
			sessionStorage.setItem("tmiddlename",data.middlename);
			//sessionStorage.setItem("tprefix",data.prefix);
			sessionStorage.setItem("taddress1",data.address1);
			sessionStorage.setItem("taddress2",data.address2);
			sessionStorage.setItem("tcity",data.city);
			sessionStorage.setItem("tzipcode",data.zipcode);
			sessionStorage.setItem("tstate",data.state);
			sessionStorage.setItem("thomephone",data.homephone);
			sessionStorage.setItem("tworkphone",data.workphone);
			sessionStorage.setItem("tmobile",data.mobile);
			sessionStorage.setItem("tid",data.id);
			sessionStorage.setItem("tein",data.ein);
			sessionStorage.setItem("tlicense",data.license);
			sessionStorage.setItem("tflag",data.flagid);
			//sessionStorage.setItem("tmark",data.mark);
			sessionStorage.setItem("ttherapistid",data.therapistid);
			
			
			
		}
		
	function renderErrorMessage(jqXHR, x, errorThrown){
		
		var message;
		var statusErrorMap = {
				'400' : "Server understood the request, but request content was invalid.",
				'401' : "Unauthorized access.",
				'403' : "Forbidden resource can't be accessed.",
				'500' : "Internal server error.",
				'503' : "Service unavailable."
		};
		if (jqXHR.status) {
			message =statusErrorMap[jqXHR.status];
			if(!message){
				message="Unknown Error \n.";
			}
		}else if(errorThrown=='parsererror'){
			message="Error.\nParsing JSON Request failed.";
		}else if(errorThrown=='timeout'){
			message="Request Time out.";
		}else if(errorThrown=='abort'){
			message="Request was aborted by the server";
		}else {
			message="Unknown Error \n.";
		}
		$("#successmessage").hide();
		$("#errormessage").html("");
		$("#errormessage").show().html("Error:"+message);  

	}
		//To add flag value

		$('.btnAdd').click(function(e){
			
			if(formid == 'flagSearch'){

				$("#successmessage").hide();

				var buttonValue= $(this).attr("value");
				var updflagname="";
				var flagsearchval="";			 
				var resourceURL ="";
				var httpType = "";
				if(buttonValue == 'Add'){
					httpType = 'GET';
					resourceURL= rooturl  + 'addFlag/' + $('#newflag').val();
				}
				else if(buttonValue == 'Update'){
					httpType = 'POST';			
					updflagname = $("#newflag").val();
					flagsearchval = $("#flagsearchtext").val();        	        	
					JSONObject= {"flagid":flagid, "flagname":updflagname};
					jsonData = JSON.stringify(JSONObject); 
					resourceURL= rooturl;
				}
				$('#addFlag').validate({
					rules: {
						newflag: {
							required: true
						}
					},
					messages: {
						newflag: "* Required Field"
					},
					submitHandler: function(form) {
						//if(flagid.length > 0 || buttonValue == 'Update'){
						//if (buttonValue == 'Update')
							$.ajax({
								type: httpType,
								contentType: 'application/json',
								url: resourceURL,
								async:false,
								dataType: "json",  
								data: jsonData,
								cache: false,
								success: function(data, textStatus, error){ 
									if(buttonValue == 'Add'){
										$('#newflag').attr('readonly', true);
										$(".btnAdd").attr('disabled', true);
										$("#errormessage").hide();
										$("#successmessage").show().html('');
										$("#successmessage").show().html("Saved Successfully");
									}
									else if(buttonValue == 'Update'){
										$('#newflag').val('');
										$(".btnAdd").attr('disabled', true);
										$('#newflag').attr('readonly', true);
										$("#errormessage").hide();
										$("#successmessage").show().html('');
										$("#successmessage").show().html("Updated Successfully"); 
									}
								},
								error: function(jqXHR, x, errorThrown){
									renderErrorMessage(jqXHR, x, errorThrown);
								}
							});
					
					}


				});


			}
			else if(formid == 'searchactivity'){
				
				$("#successmessage").hide();

				var buttonValue= $(this).attr("value");
				
				var resourceURL ="";
				var httpType = "";
				if(buttonValue == 'Add'){
					
					httpType = 'POST';
					JSONObject= {"activitytype":$('#aType').val(), "rate":$('#rate').val(),"cptcode":$('#cptcode').val(),
							"rateper":$('#rateper').val(),"desc":$('#desc').val()
					};

					jsonData = JSON.stringify(JSONObject); 
				
					resourceURL= rooturl  + 'addActivityType/';
				}
				else if(buttonValue == 'Update'){
					
					httpType = 'POST';	
					JSONObject= {"typeid":actTypeid,"activitytype":$('#aType').val(), "rate":$('#rate').val(),"cptcode":$('#cptcode').val(),
							"rateper":$('#rateper').val(),"desc":$('#desc').val()
					};
					jsonData = JSON.stringify(JSONObject); 
					resourceURL= rooturl + 'updateActivityType/';;
					
				}
				$('#activity').validate({
					rules: {
						aType: {
							required: true
						},
						rate:{
							required: true
						},
						cptcode:{
							required: true
						},
						rateper:{
							required: true
						},
						desc:{
							required: true
						}
					},
					messages: {
						aType: "*",
						rate: "*",
						cptcode: "*",
						rateper: "*",
						desc: "*"
					},
					submitHandler: function(form) {
						
						//if(buttonValue == 'Add'){
							$.ajax({
								type: httpType,
								contentType: 'application/json',
								url: resourceURL,
								async:false,
								dataType: "json",  
								data: jsonData,
								success: function(data, textStatus, error){ 
									if(buttonValue == 'Add'){
										
										$("#activity :input").attr("disabled", true);
										$("#errormessage").hide();
										$("#successmessage").show().html('');
										$("#successmessage").show().html("Saved Successfully");
									}
									else if(buttonValue == 'Update'){
										
										$(".btnAdd").attr('value', 'Add');
										$("#activity :input").attr("disabled", true);
										$("#errormessage").hide();
										$("#successmessage").show().html('');
										$("#successmessage").show().html("Updated Successfully"); 
									}
								},
								error: function(jqXHR, x, errorThrown){
									renderErrorMessage(jqXHR, x, errorThrown);
								}
							});
						
					}


				});
			}
		

		});
		
});
} )( jQuery );