/*!

=========================================================
* Paper Kit React - v1.0.0
=========================================================

* Product Page: https://www.creative-tim.com/product/paper-kit-react

* Copyright 2019 Creative Tim (https://www.creative-tim.com)
* Licensed under MIT (https://github.com/creativetimofficial/paper-kit-react/blob/master/LICENSE.md)

* Coded by Creative Tim

=========================================================

* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

*/
import React , {Component} from "react";
import axios from 'axios';
import "assets/css/bootstrap.min.css";
// reactstrap components
import { Button, Form, FormGroup, Input, Container, Row, Col, Modal, FormText } from "reactstrap";
import OverlayTrigger from 'react-bootstrap/OverlayTrigger'
import StarRatings from 'react-star-ratings';
import Popover from 'react-bootstrap/Popover'
import { withRouter } from "react-router-dom";
// core components
import ExamplesNavbar from "components/Navbars/ExamplesNavbar.js";
import ProfilePageHeader from 'components/Headers/ProfilePageHeader.js';


import {NotificationContainer, NotificationManager} from 'react-notifications';
import "../../../node_modules/react-notifications/lib/notifications.css"
import "../../../node_modules/react-notifications/lib/Notifications.js"

class PatientPage extends Component {
  constructor(props)
  {
    super(props);
    this.state = {
      showProfile: false ,      
      registerShow: false,
      loginShow: false,
      name: "" ,
      surname: "" ,
      email: "" ,
      jbo: "" ,
      password: "",
      passConf: "" ,
      phone: "" ,
      adress: "" ,
      city: "" ,
      statee: "" ,
      formValid: false  ,   // enable/disable submit button depending on form validation
      notFilledError: false ,
      formErrorText: "" ,
      passErrorText: "" ,
      emailErrorText: "" ,
      emailValid: true ,
      showResponse: false ,
      loginDisable: true ,
      message: "" ,
      clinics: [] ,
      types: [] ,
      pretragaTip: "" ,  
      pretragaDatum: "" ,
      pretragadIme: "" ,  
      pretragadPrezime: "" ,
      pretragadOcena: 0 ,
      hiddenForm: true ,   // search form for clinics
      filterOcena: 0   ,   // 0 for rating, 1 for checkup price
      doctors: [] ,
      hideKlinike: true ,
      choosenClinic: "", 
      selectedTermin: "" ,
      selectedDoctor: null ,
      showTermin: false ,
      showAppointment: false ,
      hideDocSearch: true , //hide form for searching doctors
      hideFilter: true ,
      password1: "" ,
      password11: "" ,
      changePass: false ,
      pretragaHappened: false , 
      showKarton: true ,
      checkupsHistory: [] ,
      icomingCheckups: [] ,
      // STATES FOR MEDICAL RECORD, NOT EDITABLE
      mrHeight: 0 ,
      mrWeight: 0 ,
      mrBloodType: "" ,
      mrDiopt: 0 ,
      diagnoses: [] ,
      tableHistory: true ,      
      hideDokore: true , 
      pops: false ,  // flags for show/hide popover
      ratingD: 0 ,
      ratingCl: 0 ,
      //ovadve do kraja states novo
      enableMedRecChange: false,
      buttonText: 'Izmeni',
      checkupStarted: false,
      mrID: 0,
      diagnose: '',
      diagValue: '',
      codebooks: [],
      showMedications: false,
      showDiagnoses: false,
      showNoviPregled: false,
      informations: '',
      codes: [],
      temp:[],
      checkup: {},
      tipPregleda: "PREGLED",
      checkupExist: false ,
      historyType: "PREGLED" ,
      operationsHistory: [] ,
      incomingOperations: [] ,
      chooseTip: true ,
      infoDijagnoza: "" ,
      infoRecepti: [] ,
      infoInfo: "" ,
      sala: "" ,
      sort1: true,
      sort2: true,
      sort3: true,
      sort4: true,
      hideOperacije: true,
      canAccessToMedicalRecord: false,
      selectedReport: {checkUp:{
        medicalWorker:{
          user:{
            id: 0
          }
        }
      }} ,
      role: "",
      ulogovani: {}
    };

    this.editPatient = this.editPatient.bind(this) ;
    this.validateEmptyFields = this.validateEmptyFields.bind(this) ;    
    this.handleMatching = this.handleMatching.bind(this) ;
    this.onDismiss = this.onDismiss.bind(this) ;
    this.cancelSearch = this.cancelSearch.bind(this) ;
    this.getProfile = this.getProfile.bind(this) ;
    this.getAllClinics = this.getAllClinics.bind(this) ;
    this.getAllCheckupTypes = this.getAllCheckupTypes.bind(this) ;
    this.srchClinics = this.srchClinics.bind(this) ;
    this.clinicFilter = this.clinicFilter.bind(this) ;
    this.doctorsFromClinic = this.doctorsFromClinic.bind(this) ;
    this.setSelected = this.setSelected.bind(this) ;
    this.medicalRequest = this.medicalRequest.bind(this) ;
    this.changePassword = this.changePassword.bind(this) ;
    this.logoutUser = this.logoutUser.bind(this) ;
    this.redirect = this.redirect.bind(this) ;
    this.toggle = this.toggle.bind(this);
    //
    this.startCheckup = this.startCheckup.bind(this);
    this.getUlogovani = this.getUlogovani.bind(this);
    this.loadCheckup = this.loadCheckup.bind(this);
  }
      
  doc = document.documentElement.classList.remove("nav-open");
  /*eff = useEffect(() => {
    document.body.classList.add("register-page");
    return function cleanup() {
      document.body.classList.remove("register-page");
    };
  });*/

  componentDidMount(){
    this.getUlogovani();
    let role = localStorage.getItem('role');
    this.getProfile();
    if (role === 'PACIJENT'){
      
      this.setState({canAccessToMedicalRecord:true})      
    }
    if (role === 'DOKTOR'){
      this.loadCheckup();
      this.pristupiPacijentu();
    }
    if(role === 'MEDICINAR'){
    }
   }

   redirect = (event) => {   
      let clinicId = event.target.parentElement.parentElement.getAttribute('data-key') 
      this.props.history.push('/clinic-homepage/' + clinicId);    
  }

  getUlogovani = () => {
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
    axios({
      method: 'get' ,    
      url: 'http://localhost:8099/getUser' ,           
      headers: { "Authorization": AuthStr }   
    }).then((response) => {
      if (response.data != null)
      {this.setState({ulogovani: response.data})}
    }, (error) => {
      this.setState({message: "Neuspesno dobavljanje korisnika.", showResponse: true}) ;
  });
}

pristupiPacijentu(){

  const {id} = this.props.match.params;
 let token = localStorage.getItem("ulogovan")
 let AuthStr = 'Bearer '.concat(token);
 axios({
   method: 'get',
   url: 'http://localhost:8099/canAccessToMedicalRecord/' + id,
   headers: { "Authorization": AuthStr }  ,
 }).then((response) => {
   console.log(response);
     if(response.data === "DA"){
      this.setState({canAccessToMedicalRecord:true})
     }
     else{
       this.setState({canAccessToMedicalRecord:false});
     }
 }, (error) => {
   console.log(error);
 });
  
}

  changeRatingDoctor = (rating, name) => {
    let params = [] ;
    params.push(name) ;
    params.push(rating) ;
    let AuthStr = 'Bearer '.concat(localStorage.getItem('ulogovan'))

    axios({
      method: 'post',
      url: 'http://localhost:8099/rateMedicalWorker'  ,
      headers: { "Authorization": AuthStr }  ,
      data: params
    }).then((response)=>{       
      if (response.status !== 200) {
        
        NotificationManager.info('Moguce je samo jednom ocijeniti doktora po pregledu!', 'Info!', 3000);
         // Alert("Moguće je oceniti samo jednom doktora po pregledu.")
      }  else {
        NotificationManager.info('Doktor uspešno ocenjen!', 'Info!', 3000);
        this.setState({ratingD: rating})
      }    
    },(error)=>{
      NotificationManager.info('Moguce je samo jednom ocijeniti doktora po pregledu!', 'Info!', 3000);
    });
  }

  changeRatingClinic = (rating, name) => {
    let params = [] ;
    params.push(name) ;
    params.push(rating) ;
    let AuthStr = 'Bearer '.concat(localStorage.getItem('ulogovan'))

    axios({
      method: 'post',
      url: 'http://localhost:8099/clinic/rateClinic'  ,
      headers: { "Authorization": AuthStr }  ,
      data: params
    }).then((response)=>{       
      if (response.status === 200) {
        NotificationManager.info('Klinika je uspešno ocenjena', 'Info!', 3000);
      }      
    },(error)=>{
      NotificationManager.error('Kliniku je moguce ocjeniti samo jednom po pregledu', 'Info!', 3000);
    });
  }

  toggle(index) {
    this.setState({
     pops: !this.state.pops
    });
  }

   logoutUser = () => {  
     localStorage.removeItem('ulogovan')
     localStorage.removeItem('role')
     this.props.history.push('/register-page');
    
   }

   clinicFilter = () => {
    let parametar = this.state.filterOcena
    let klinike = this.state.clinics
    if (parametar === "")
      alert("polje za filtriranje je prazno")
    else {

      axios({
        method: 'post',
        url: 'http://localhost:8099/clinic/filterClinic/' + parametar ,
        data: klinike
      }).then((response)=>{       
        this.setState({clinics: response.data}) ;
        
      },(error)=>{
        console.log(error);
      });
    }
  }

  doctorsFromClinic = (event) => {
    let tp = this.state.pretragaTip ;
    let date = this.state.pretragaDatum ;
    let key = event.target.getAttribute('data-key')
    this.setState({choosenClinic: key})
    let parametri = []
    parametri.push(key)
    parametri.push(tp)
    parametri.push(date)

    axios({
      method: 'post',
      url: 'http://localhost:8099/clinic/clinicDoctors' ,
      data: parametri     
    }).then((response)=>{       
      this.setState({hideDokore: false, hideKlinike: true ,doctors: response.data, hiddenForm: true, hideDocSearch: false, hideFilter: true}) ;
      
    },(error)=>{
      console.log(error);
    });
  }

  cancelSearch = () => {
    this.setState({hiddenForm: false, hideFilter: true, pretragaHappened: false})
    this.getAllClinics();
  }

  changePassword = (event) => {
    event.preventDefault();

    let newp = [] ;
    newp.push(this.state.password1);
    let AuthStr = 'Bearer '.concat(localStorage.getItem('ulogovan'))

    axios({
      method: 'post',
      url: 'http://localhost:8099/changePass' ,
      data: newp ,
      headers: { "Authorization": AuthStr } ,      
      ContentType: 'application/json'
    }).then((response)=>{       
      localStorage.setItem('ulogovan', response.data.user.email)
      this.setState({showResponse: true, message: "Lozinka je uspesno promenjena."})
      NotificationManager.success('Lozinka je uspjesno izmjenjena', 'Uspjesno!', 3000);
    },(error)=>{
      console.log(error);
    });

    this.setState({changePass:false})
  }

   srchClinics = () => {
      let parametri = []
      parametri.push(this.state.pretragaTip)
      parametri.push(this.state.pretragaDatum)

    axios({
      method: 'post',
      url: 'http://localhost:8099/clinic/searchClinic' ,
      data: parametri
    }).then((response)=>{       
      this.setState({clinics: response.data, hideFilter: false, pretragaHappened: true}) ;
      
    },(error)=>{
      console.log(error);
    });

    this.setState({hiddenForm: true})
   }

   srchDoctors = () => {
    let parametri = []
    parametri.push(this.state.choosenClinic)
    parametri.push(this.state.pretragaTip)
    parametri.push(this.state.pretragaDatum)    
    parametri.push(this.state.pretragadIme)
    parametri.push(this.state.pretragadPrezime)
    parametri.push(this.state.pretragadOcena)

  axios({
    method: 'post',
    url: 'http://localhost:8099/searchDoctors' ,
    data: parametri
  }).then((response)=>{       
    this.setState({doctors: response.data}) ;
    
  },(error)=>{
    console.log(error);
  });

  this.setState({hiddenForm: true})
 }

  medicalRequest = () => {
    
      let clinic = {
        "name":this.state.selectedDoctor.clinic.name
      }

      let user = {
        "name":this.state.selectedDoctor.user.name ,
        "surname":this.state.selectedDoctor.user.surname
      }

      let mw = {
        "user" : user,
        "id":this.state.selectedDoctor.id
      }

      let chType = {
        "name" : this.state.pretragaTip
        
      }

      let checkup = {
        "clinic": clinic ,
        "date" : this.state.pretragaDatum ,
        "time": this.state.selectedTermin ,
        "medicalWorker" : mw ,
        "type" : "PREGLED",
        "checkUpType": chType
      }

      let AuthStr = 'Bearer '.concat(localStorage.getItem('ulogovan'))

      axios({
        method: 'post',
        url: 'http://localhost:8099/checkup/checkupRequest' ,
        headers: { "Authorization": AuthStr }  ,
        data: checkup     
      }).then((response)=>{ 
        if (response.status === '200') {
          this.setState({message: "Uspešno ste poslali zahtev za zakazivanje pregleda", showAppointment: false})
          NotificationManager.success('Uspešno ste poslali zahtev za zakazivanje pregleda!', 'Uspjesno!', 3000);
        } else {
          NotificationManager.info('Ne mozete posleti zahtjev za rigistraciju!', 'Info!', 3000);

         // alert('NE MOZE DA ZAKAZE')
        }
        
        
      },(error)=>{
        alert("GRESKAAA")
        console.log(error);
      });
  }

   getAllCheckupTypes = () => {
     let tipovi = []
     let i;

    axios({
      method: 'get',
      url: 'http://localhost:8099/checkUpType/allTypes'      
    }).then((response)=>{  
      for (i=0 ; i < response.data.length ; i++) {
        tipovi.push(response.data[i].name)
      }
      this.setState({types: tipovi}) ;
      if(response.data.length > 0){
        this.setState({pretragaTip:response.data[0].name});
      }
    },(error)=>{
      console.log(error);
    });
   }

  validateEmptyFields = () => {     
      if (this.state.name === "" || this.state.surname === "" || this.state.email === "" || this.state.jbo === "" || this.state.phone === "" || this.state.adress === "" || this.state.city === "" || this.state.statee === "" )
      {
        this.setState({notFilledError:true});
      }
  }


  onDismiss = () => this.setState({showResponse: !this.showResponse});

  getAllClinics = () => {
    axios({
      method: 'get',
      url: 'http://localhost:8099/clinic/getClinics'      
    }).then((response)=>{    
      this.getAllCheckupTypes();   
      this.setState({clinics: response.data, chooseTip: true, hideOperacije: true,
      hideDokore: true, tableHistory: true, hideKlinike: false, hiddenForm: false, showKarton: true, showProfile: true})
    },(error)=>{
      console.log(error);
    });
  }

  handleMatching = (event) => {
    if (this.state.password1 !== event.target.value)
      {
        this.setState({formValid: true, passErrorText: "Lozinke se ne poklapaju."});    // disabling submit button
      } else {
        this.setState({formValid: false, passErrorText: ""});
      }
  }

  getProfile = () => {
    //this.setState({showProfile: false}) ;
    let AuthStr = 'Bearer '.concat(localStorage.getItem("ulogovan"));
    const {id} = this.props.match.params; 
    axios({
      method: 'get',
      url: 'http://localhost:8099/getPatientProfile/' + id,
      headers: { "Authorization": AuthStr }
    }).then((response)=>{      
      this.setState({name:response.data.user.name, email:response.data.user.email,surname:response.data.user.surname,phone:response.data.phoneNumber,password:response.data.user.password,
                    jbo: response.data.jbo , adress: response.data.address , city: response.data.city , statee : response.data.state
      
      })
      
    },(error)=>{
      console.log(error);
    });
  }

  editPatient = event => {
        event.preventDefault();

        this.validateEmptyFields();
        
        if (this.state.notFilledError)
        {
            this.setState({formErrorText: "Sva polja su obavezna. Molim vas popunite prazna polja."})
        } else {

          let user = {
            "name": this.state.name ,
            "surname": this.state.surname ,
            "email": this.state.email ,
            "password":this.state.password,
            "enabled": true
          };

          let data = {
            "user": user,
            "jbo": this.state.jbo ,
            "phoneNumber": this.state.phone ,
            "address": this.state.adress ,
            "city": this.state.city ,
            "state": this.state.statee,
            "email": this.state.email ,
          };

          axios({
            method: 'post',
            url: 'http://localhost:8099/editPatient', 
            data: data ,
            ContentType: 'application/json'            
          }).then((response) => {
            if (response.status === 200)
              {
                if (response.data != null)
                NotificationManager.success('Uspešno ste izmjenili podatke!', 'Uspjesno!', 3000);

                this.setState({name:response.data.user.name, email:response.data.user.email,surname:response.data.user.surname,phone:response.data.phoneNumber,password:response.data.user.password,
                  jbo: response.data.jbo , adress: response.data.adress , city: response.data.city , statee : response.data.state
    
    })
              }
          }, (error) => {
            console.log(error);
          });          
          
      }
  };

  setSelected = event => {
    let niz = []
    let doc = event.target.parentElement.parentElement.getAttribute('data-key')
    let AuthStr = 'Bearer '.concat(localStorage.getItem("ulogovan"));
    niz.push(doc)
    niz.push(this.state.pretragaDatum)
    axios({
      method: 'post',
      url: 'http://localhost:8099/clinic/getSelectedDoctor',
      headers: { "Authorization": AuthStr } ,
      data: niz
    }).then((response)=>{      
      this.setState({selectedDoctor: response.data, showTermin: true})
      
    },(error)=>{
      console.log(error);
    });
  }

   
  getMedicalRecord = () => {
    let AuthStr = 'Bearer '.concat(localStorage.getItem("ulogovan"));
    const {id} = this.props.match.params;
    axios({
      method: 'get',
      url: 'http://localhost:8099/getMedicalRecord/' + id,
      headers: { "Authorization": AuthStr }
    }).then((response)=>{      
      this.setState({mrHeight: response.data.height , mrWeight: response.data.weight, 
        mrBloodType: response.data.bloodType, mrDiopt: response.data.diopter, diagnoses: response.data.diagnoses,
        mrId: response.data.id, hideDokore: true, tableHistory: true, showKarton: false, hiddenForm: true, hideKlinike: true, hideDoctors: true, showProfile: true, chooseTip: true, hideOperacije: true
      })
    },(error)=>{
      console.log(error);
    });
  }

  displayCheckups = (event) => {
    let type = this.state.historyType ;
    //this.setState({historyType: type})
    let AuthStr = 'Bearer '.concat(localStorage.getItem("ulogovan")); 
    const {id} = this.props.match.params;   
    axios({
      method: 'post',
      url: 'http://localhost:8099/checkup/patientHistory/' + type + '/' + id ,
      headers: { "Authorization": AuthStr } ,     
    }).then((response)=>{     
      if (type === 'PREGLED') {
        this.setState({checkupsHistory: response.data["2"], icomingCheckups: response.data["1"], showProfile: true, 
        showKarton: true, hideClinics: true, tableHistory: false, hideDokore: true, hideDocSearch: true, hideOperacije: true})
      } else {
        this.setState({operationsHistory: response.data["2"], incomingOperations: response.data["1"], showProfile: true, 
      showKarton: true, hideClinics: true, tableHistory: true, hideDokore: true, hideDocSearch: true, hideOperacije: false})
      }
      
      //this.initPops(this.state.checkupsHistory.length)  // valjda ce se prvo setovati, pliz gad
    },(error)=>{
      console.log(error);
    });
  }

  editMedicalRecord = () => {
    if(!this.state.enableMedRecChange){
      this.setState({enableMedRecChange: true, buttonText:'Sačuvaj izmene'})   
    }
    else{
      this.setState({enableMedRecChange: false, buttonText:'Izmeni'});
      let data = {
        id: this.state.mrId,
        bloodType: this.state.mrBloodType,
        diopter: this.state.mrDiopt,
        height: this.state.mrHeight,
        weight: this.state.mrWeight
      }
      axios({
        method: 'post',
        url: 'http://localhost:8099/editMedicalRecord',
        data: data,
        ContentType: 'application/json'
    }).then((response) => {
        console.log(response);
        NotificationManager.success('Uspjesna izmjena zdravstvenog kartona!', 'Uspjesno!', 3000);

    },(error) => {
        console.log(error);
        NotificationManager.error('Greska izmjene zdravstvenog kartona!', 'Greska!', 3000);

    });
    }
  }

  sortByOcjena(){
    let temp = this.state.clinics;
    if(this.state.sort4){
      temp.sort(function(a,b){let ime1 = a.rating; let ime2 = b.rating; return ime2-ime1})
    }
  else{
    temp.sort(function(a,b){let ime1 = a.rating; let ime2 = b.rating; return ime1-ime2})
  }
    this.setState({clinics:temp,sort4: !this.state.sort4})
  }

  sortByOcjenaDoc(){
    let temp = this.state.doctors;
    if(this.state.sort4){
      temp.sort(function(a,b){let ime1 = a.rating; let ime2 = b.rating; return ime2-ime1})
    }
  else{
    temp.sort(function(a,b){let ime1 = a.rating; let ime2 = b.rating; return ime1-ime2})
  }
    this.setState({doctors:temp,sort4: !this.state.sort4})
  }

  sortByAdresa(){
    let temp = this.state.clinics;
    if(this.state.sort3){
      temp.sort(function(a,b){let ime1 = a.address; let ime2 = b.address; return ime2.localeCompare(ime1)})
    }
  else{
    temp.sort(function(a,b){let ime1 = a.address; let ime2 = b.address; return ime1.localeCompare(ime2)})
  }
    this.setState({clinics:temp,sort3: !this.state.sort3})
  }
  
sortByNazivDoc(value){
  let temp = this.state.doctors;
  if(this.state.sort1){
    temp.sort(function(a,b){let ime1 = a.name; let ime2 = b.name; return ime2.localeCompare(ime1)})
  }
else{
  temp.sort(function(a,b){let ime1 = a.name; let ime2 = b.name; return ime1.localeCompare(ime2)})
}
  this.setState({doctors:temp,sort1: !this.state.sort1})
}

sortByCity(){
  let temp = this.state.clinics;
  if(this.state.sort2){
    temp.sort(function(a,b){let ime1 = a.city; let ime2 = b.city; return ime2.localeCompare(ime1)})
  }
else{
  temp.sort(function(a,b){let ime1 = a.city; let ime2 = b.city; return ime1.localeCompare(ime2)})
}
  this.setState({clinics:temp,sort2: !this.state.sort2})
}



  editReport = () => {
    if(!this.state.enableMedRecChange){
      this.setState({enableMedRecChange: true, buttonText:'Sačuvaj izmene'})   
    }
    else{
      this.setState({enableMedRecChange: false, buttonText:'Izmeni'});
      let report = this.state.selectedReport;
      report.informations = this.state.infoInfo;
      let token = localStorage.getItem("ulogovan")
      let AuthStr = 'Bearer '.concat(token);
      axios({
        method: 'post',
        url: 'http://localhost:8099/checkup/updateReport',
        data: report,
        ContentType: 'application/json',
        headers: { "Authorization": AuthStr }  
    }).then((response) => {
        console.log(response);
        NotificationManager.success('Uspjesna izmjena izvjestaja o pregledu', 'Uspjesno!', 3000);

    },(error) => {
        console.log(error);
        NotificationManager.error('Greska izmjene izvjestaja o pregledu', 'Greska!', 3000);
    });
         
    }
  }

  startCheckup = event =>{
    event.preventDefault();
    axios({
        method: 'get',
        url: 'http://localhost:8099/codebook',
      }).then((response)=>{
        console.log(response);
        this.setState({codes:response.data})
      },(error)=>{
        console.log(error);
      });
    this.setState({checkupStarted:true, canAccessToMedicalRecord:true});
  }

  descriptionValidation(e){
    this.setState({informations:e.target.value})
    let desc = e.target.value
    if (desc === undefined || desc === "")
        this.setState({descVal:"Opis je obavezno polje"})
    else
        this.setState({descVal:""})
}

zakaziPregled = event =>{
  event.preventDefault();
  this.setState({showNoviPregled:true});
}

finishCheckup = event => {
event.preventDefault();

let informations = this.state.informations;
let ok = true;

if (informations === undefined || informations === '') {
    this.setState({ descVal: "Niste uneli informacije o pregledu." })
    ok = false;
}
else
    this.setState({ descVal: "" })

if (ok) {
    let data = {
        "informations": this.state.informations,
        "diagnose":this.state.diagnose.code,
        "checkUp":this.state.checkup
    };
    axios({
        method: 'post',
        url: 'http://localhost:8099/checkup/addReport',
        data: data,
        ContentType: 'application/json'
    }).then((response) => {
        console.log(response);
       // alert("Izvještaj o pregledu je uspješno sacuvan u karton pacijenta.")
       NotificationManager.success('Izvještaj o pregledu je uspješno sacuvan u karton pacijenta.', 'Uspjesno!', 3000);


        axios({
            method: 'post',
            url: 'http://localhost:8099/checkup/addRecipes/'+response.data.id,
            data: this.state.codebooks,
            ContentType: 'application/json'
        }).then((response) => {
            console.log(response);
        },(error) => {
            console.log(error);
           // alert("Greska prilikom dodavanja recepata");
           NotificationManager.error('Greska prilikom dodavanja recepata', 'Greska!', 3000);

        });

    }, (error) => {
        console.log(error);
    });
}
};

zakazi = event =>{
  event.preventDefault();
  let token = localStorage.getItem("ulogovan")
  let AuthStr = 'Bearer '.concat(token);

  let time = this.state.vrijeme;
  let tip = this.state.tipPregleda;
  let ok = true;
  let number = this.state.vrijeme;


  
  let ddatum = this.state.datumNovog;
  let godina;
  let mjesec;
  let dan;
  godina = ddatum[0] + ddatum[1]+ddatum[2]+ddatum[3]

  mjesec = ddatum[5] + ddatum[6]

  dan = ddatum[8] + ddatum[9]

  let newDate = new Date();
  let date = parseInt(newDate.getDate())
  let month = parseInt(newDate.getMonth() + 1);
  let year = parseInt(newDate.getFullYear());
  godina = parseInt(godina)
  mjesec = parseInt(mjesec)
  dan = parseInt(dan)

  let datumN = this.state.datumNovog;
 
 
if(ddatum=== undefined || ddatum === ''){
  ok = false;
  this.setState({messageDate1:"Datum je obavezno polje"})
}
  else if(godina < year){
   ok = false
    this.setState({messageDate1:"Godina je prosla"})
  }
  else if(mjesec< month && godina<=year){
    this.setState({messageDate:"Mjesec je prosao"})
    this.setState({messageDate1:"Mjesec je prosao"})
  }
  else if(dan<date && mjesec<=month && godina<=year ){
    ok = false;
    this.setState({messageDate:"Dan je prosao"})
  }
  else { 
    this.setState({messageDate1:""})
}
 



if (number === undefined || number === ''){
  this.setState({messageTime:"Vrijeme pregleda je obavezno polje"})
  ok = false;
}
else if (number < 8 || number > 18){
  this.setState({messageTime:"Radno vrijeme je od 8-18h"})
  ok = false;
}
else
  this.setState({messageTime:""})
  if(ok){
      let params = {
          "patient": this.state.checkup.patient,

                  "scheduled":false,
                  "type": tip,
                  "time": time,
                  "date": datumN,
      }

      axios({
          method: 'post',
          url: 'http://localhost:8099/bookForPatient',
          headers: { "Authorization": AuthStr } ,
          data: params,
          ContentType: 'application/json',
        }).then((response)=>{      
          NotificationManager.success('Uspjesno ste poslali zahtjev za zakazivanje pregleda', 'Uspjesno!', 3000);
          this.setState({pacijenti: response.data}) ;
          this.setState({tipPregleda:"PREGLED" , vrijeme:"", showNoviPregled:false})
        },(error)=>{
          console.log(error);
          this.setState({messagePacijent: "Ne postoji pacijent "})
          NotificationManager.error('Greska slanja zahtjeva za zakazivanje pregleda', 'Greska!', 3000);

        });
  }

}

handleOptionChange(changeEvent) {
  let codebook = this.state.temp;
  if(codebook.includes(changeEvent.target.value)){
      const items = codebook.filter(item => item !== changeEvent.target.value);
      this.setState({ temp: items });
  }
  else{
      const items = codebook.filter(item => item !== changeEvent.target.value);
      items.push(changeEvent.target.value)
      this.setState({temp: items});
  }
};

dateValidation(e){
    
  let ddatum = e.target.value;
  let godina;
  let mjesec;
  let dan;
  godina = ddatum[0] + ddatum[1]+ddatum[2]+ddatum[3]

  mjesec = ddatum[5] + ddatum[6]

  dan = ddatum[8] + ddatum[9]

  let newDate = new Date();
  let date = parseInt(newDate.getDate())
  let month = parseInt(newDate.getMonth() + 1);
  let year = parseInt(newDate.getFullYear());
  godina = parseInt(godina)
  mjesec = parseInt(mjesec)
  dan = parseInt(dan)
 
 
if(ddatum=== undefined || ddatum === ''){
  this.setState({messageDate:"Datum je obavezno polje"})
  this.setState({messageDate1:"Datum je obavezno polje"})
}
  else if(godina < year){
    this.setState({messageDate:"Godina je prosla"})
    this.setState({messageDate1:"Godina je prosla"})
  }
  else if(mjesec< month && godina<=year){
    this.setState({messageDate:"Mjesec je prosao"})
    this.setState({messageDate1:"Mjesec je prosao"})
  }
  else if(dan<date && mjesec<=month && godina<=year ){
    this.setState({messageDate1:"Dan je prosao"})
    this.setState({messageDate:"Dan je prosao"})
  }
  else { 
    this.setState({messageDate:""})
    this.setState({messageDate1:""})
}
  this.setState({datumNovog:e.target.value})


};

promjenaTipaPregleda(e){
  this.setState({ tipPregleda:e.target.value});
}

timeValidation(e){
   
  this.setState({vrijeme:e.target.value})
  let number = e.target.value;
  if (number === undefined || number === '')
      this.setState({messageTime:"Vrijeme pregleda je obavezno polje"})
  else if (number < 8 || number > 18)
      this.setState({messageTime:"Radno vrijeme je od 8-18h"})
  else
      this.setState({messageTime:""})
};

loadCheckup = () => {
  // ucitati pregled kod ovog pacijenta za ulogovanog doktora ukoliko postoji za danasnji datum
  const {id} = this.props.match.params;
  let token = localStorage.getItem("ulogovan")
  let AuthStr = 'Bearer '.concat(token);
  axios({
    method: 'get',
    url: 'http://localhost:8099/checkup/getCheckup/' + id,
    headers: { "Authorization": AuthStr } ,
  }).then((response)=>{      
    this.setState({checkup: response.data, checkupExist:true}) ;
  },(error)=>{
    console.log(error);
    this.setState({checkupExist: false})
  });
}

checkupInfo = id => {
  let AuthStr = 'Bearer '.concat(localStorage.getItem("ulogovan"));    
  axios({
    method: 'post',
    url: 'http://localhost:8099/checkup/infoReport/' + id ,
    headers: { "Authorization": AuthStr } ,     
  }).then((response)=>{ 
    if (response.status === 200)
      this.setState({infoDijagnoza: response.data.report.diagnose , infoInfo: response.data.report.informations
        , showProfile: true, chooseTip: true, infoRecepti: response.data.code.name , showDetailsCheckup: true ,
      sala: response.data.report.checkUp.room.name + ' ' + response.data.report.checkUp.room.number,selectedReport:response.data.report})
    
  },(error)=>{
    console.log(error);
  });
}
  
render() {
  let role = localStorage.getItem('role');
  return (
  <div>
   <ExamplesNavbar showProfileEvent={() => this.setState({hideDokore: true, showProfile: false, hiddenForm: true, hideKlinike: true, tableHistory: true, showKarton: true, chooseTip: true, hideOperacije: true})} 
                    hideLoginEvent={true} 
                    hideRegisterEvent={true} 
                    hideNewWorker = {true}
                    hideQuickEvent = {true}
                    hideRecipes = {true}
                    hideCheckupTypes = {true}
                    hideClinic = {true}
                    hideAddClinic = {true}
                    hideCodebook = {true}
                    hideRequestsAdmin = {true}
                    hideCheckup = {true}
                    hideRooms = {true}
                    hideDoctors = {true}
                    hideClinics = {true}                      
                    hideKalendar = {true}
                    hidePatientsDoc = {true}   
                    hideVacation = {true}
                    hideAllQuicksEvent = {true}
                    hideDoctorsEvent = {true}
                    kartonEvent = {this.getMedicalRecord}
                    displayClinics = {this.getAllClinics}
                    displayHistory = {() => this.setState({chooseTip: false, showKarton: true, hiddenForm: true, hideKlinike: true, hideDoctors: true, showProfile: true , hideOperacije: true})}                       
    />
    <ProfilePageHeader />
    <div className="section profile-content">
      <Container >
        <div className="owner">            
          <div className="name">
          <Modal modalClassName="modal-register" isOpen={this.state.showDiagnoses}>
                              <div className="modal-header no-border-header text-center">
                                  <button aria-label="Close" className="close" data-dismiss="modal" type="button" onClick={() => this.setState({ showDiagnoses: false })}>
                                      <span aria-hidden={true}>×</span>
                                  </button>
                                  <h3 className="title mx-auto">Izaberite dijagnozu</h3>
                              </div>
                              <ul className="list-group">
                                  {this.state.codes.map(code => (
                                  <li key={code.code} className="list-group-item" hidden={code.type==='LIJEK'}>
                                      <label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                      <Input name="type" type="radio" checked={this.state.diagValue.code===code.code} onChange={() => this.setState({diagValue:code})}></Input>
                                      {code.code} - {code.name}
                                      </label>
                                  </li>
                                  ))}
                              </ul>
                              <div className="modal-body">
                                  <Button block className="btn-round" color="info" onClick={() => this.setState({showDiagnoses: false, diagnose:this.state.diagValue })}>
                                      Sačuvaj dijagnozu
                                  </Button>
                              </div>
                          </Modal>

                          <Modal  modalClassName="modal-register" isOpen={this.state.showNoviPregled}>
<div className="modal-header no-border-header text-center">
        <button
          aria-label="Close"
          className="close"
          data-dismiss="modal"
          type="button"
          onClick={event => this.setState({showNoviPregled:false , tipPregleda:"", timeValidation:"", messageDate1:""} )}
        >
        <span aria-hidden={true}>x</span>
        </button>
        </div>
        <div className="modal-body">
        <Form onSubmit={this.zakazi} >
     <div> 
                          <FormGroup>
                           <div >
                <Input name="datum" type="date" value={this.state.datumNovog} onChange={(event) => this.dateValidation(event)} />  
                <p style={{color:'red'}}>{this.state.messageDate1}</p>     
                </div> 
                </FormGroup>
                <FormGroup>
                       <select value={this.state.tipPregleda}
                          onChange={(e) =>  this.promjenaTipaPregleda(e)}>
                          <option value={"PREGLED"}>Pregled</option>
                          <option value={"OPERACIJA"}>Operacija</option>
                          </select> 
                          </FormGroup>
                          <FormGroup>
                          <label>Vrijeme</label>
                          <Input name="vrijeme" value={this.state.vrijeme} type="number" onChange={(event) => this.timeValidation(event)} />
                          <p style={{color:'red'}}>{this.state.messageTime}</p>    
                          </FormGroup>

                              </div>
                                  <Button block className="btn-round" color="info"   >
                              Zakazi
                                  </Button>
                                  </Form>
                          </div>
                          </Modal>


                          <Modal modalClassName="modal-register" isOpen={this.state.showMedications}>
                              <div className="modal-header no-border-header text-center">
                                  <button aria-label="Close" className="close" data-dismiss="modal" type="button" onClick={() => this.setState({ showMedications: false })}>
                                      <span aria-hidden={true}>×</span>
                                  </button>
                                  <h3 className="title mx-auto">Izaberite recepte</h3>
                              </div>
                              <ul className="list-group">
                                  {this.state.codes.map(code => (
                                  <li key={code.code} className="list-group-item" hidden={code.type==='DIJAGNOZA'}>
                                      <label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                      <Input name="type" type="checkbox" value={code.code} checked={this.state.temp.includes(code.code)} onChange={(event) => this.handleOptionChange(event)}></Input>
                                      {code.code} - {code.name}
                                      </label>
                                  </li>
                                  ))}
                              </ul>
                              <div className="modal-body">
                                  <Button block className="btn-round" color="info" onClick={(e) => this.setState({showMedications: false, codebooks:this.state.temp})}>
                                      Sačuvaj recepte
                                  </Button>
                              </div>
                          </Modal>
          </div>
        </div>          
        <br />

  <Modal  modalClassName="modal-register" isOpen={this.state.changePass}>
    <div className="modal-header no-border-header text-center">
        <button
          aria-label="Close"
          className="close"
          data-dismiss="modal"
          type="button"
          onClick={event => this.setState({changePass:false, password1:"",password11:""})} // na izlaz 
        >
        <span aria-hidden={true}>×</span>
        </button>
        </div>
        <div className="modal-body"> 
        <Form onSubmit={this.changePassword} > 
            <FormGroup>
              <label className="text-primary font-weight-bold">Nova lozinka</label>
              <Input  name="password1"  value = {this.state.password1}  onChange={(event) => this.setState({password1: event.target.value})} type="password"  />
            
            </FormGroup>
            <FormGroup>
              <label className="text-primary font-weight-bold">Potvrda lozinke</label>
              <Input  name="password11"  value = {this.state.password11} onBlur={this.handleMatching} onChange={event => this.setState({password11: event.target.value})}  type="password"  />
              <p style={{color:'red'}} > {this.state.passErrorText} </p>
            </FormGroup>
      <Button block className="btn-round" color="info" disabled={this.state.password1 !== this.state.password11}>IZMENI</Button>
      </Form>
</div>
</Modal>

<section className="bar pt-0" hidden={this.state.hiddenForm || !this.state.showProfile}>
     <div className="content"  >
             <div className="row">
             <div className="col-sm-3">
                      <div className="form-group">
                          <label>Tip pregleda</label>
                          <select id = "typeOfCheckupE2E" className="form-control" value={this.state.pretragaTip} onChange = {event => this.setState({pretragaTip: event.target.value})} >
                          {this.state.types.map(type => (
                              <option key={type}>
                                  {type}                                    
                              </option>
                            ))} 
                          </select>                            
                      </div>
                    </div>    
                                     
                <div className="col-sm-3 col-md-3">
                      <div className="form-group">
                        <label>Datum</label>
                        <input id = "dateOfCheckupE2E" type="date" className="form-control" value={this.state.pretragaDatum} onChange = {event => this.setState({pretragaDatum: event.target.value})}/>
                      </div>
                    </div>                                          
                                         
                    <div className="col-sm-3">
                    <div className="form-group">
                      <label style={{color: "white"}}>gbfhtjry</label>
                      <Button id="searchE2E" onClick={this.srchClinics} block className="btn-round form-control" color="info">Pretraži</Button>
                    </div> </div>
                    </div>                      
       </div>
</section>
<div>
<div id="ZdravstveniKarton" hidden={this.state.showKarton  || !this.state.canAccessToMedicalRecord }>
<section className="bar pt-0" >
  <h1 className="text-info font-weight-bold align-center">Zdravstveni karton</h1>    
        <form className="border border-info rounded" style={{marginLeft:"auto", marginRight:"auto", padding:"10px"}}>
      <div className="form-row">
        <FormGroup className="col-md-4">
          <label for="inputEmail4">Visina (cm):</label>
          <Input type="text" disabled={role === 'PACIJENT' || this.state.enableMedRecChange === false} value={this.state.mrHeight} onChange={(event) => this.setState({mrHeight: event.target.value})}/>
        </FormGroup>
        <FormGroup className="col-md-4">
          <label>Težina (kg):</label>
          <Input type="text" disabled={role === 'PACIJENT' || this.state.enableMedRecChange === false} value={this.state.mrWeight} onChange={(event) => this.setState({mrWeight: event.target.value})}/>
        </FormGroup>
      </div>
      <div className="form-row">
      <FormGroup className="col-md-4">
        <label>Krvna grupa:</label>
        <Input type="text" disabled={role === 'PACIJENT' || this.state.enableMedRecChange === false} value={this.state.mrBloodType} onChange={(event) => this.setState({mrBloodType: event.target.value})}/>
      </FormGroup>
      <FormGroup className="col-md-4">
        <label>Dioptrija:</label>
        <Input type="text" disabled={role === 'PACIJENT' || this.state.enableMedRecChange === false} value={this.state.mrDiopt} onChange={(event) => this.setState({mrDiopt: event.target.value})}/>
      </FormGroup>    
      <FormGroup className="col-md-4">
        <label>&nbsp;</label>
        <Button hidden={!this.state.checkupStarted} block className="btn-round" color="info" onClick={() => this.editMedicalRecord()}>
          {this.state.buttonText}
        </Button>       
      </FormGroup>    
      </div>    
    </form>
    </section>
    </div>
    <br></br>
    <div className = "row" hidden = {!this.state.checkupExist}>
      <div  className="col-md-12">
        <div className="col-md-4" >
          <Button block className="btn-round" color="info" onClick={(event) => this.startCheckup(event)} hidden={this.state.checkupStarted}>
            Započni pregled
          </Button>
        </div>
        <div hidden={!this.state.checkupStarted}>
                                  <FormGroup>
                                  <label>Informacije o pregledu:</label>
                                  <Input type="textarea" value={this.informations} onChange={(event) => this.descriptionValidation(event)}></Input>
                                  <p style={{ color: 'red' }}>{this.state.descVal}</p>
                                  </FormGroup>
                                  <FormGroup>
                                  <label>Dijagnoza: <ul hidden={this.state.diagnose=== ''}>
                                          <li>Šifra: {this.state.diagnose.code}</li>
                                          <li>Naziv: {this.state.diagnose.name}</li>
                                      </ul></label>
                                  </FormGroup>
                                  <FormGroup>
                                      <Button className="btn-round" color="info" outline onClick={event => this.setState({ showDiagnoses: true })}>
                                          <i className="fa fa-cog" /> Izaberi dijagnozu
                                      </Button>
                                  </FormGroup>
                                  <FormGroup>
                                  <label>Izabrano recepata: {this.state.codebooks.length}</label>
                                  </FormGroup>
                                  <FormGroup>
                                      <Button className="btn-round" color="info" outline onClick={event => this.setState({ showMedications: true })}>
                                          <i className="fa fa-cog" /> Recepti
                                      </Button>
                                  </FormGroup>
                                  <Row>
                                  <div class = 'col-md-3'>
                                  <Button block className="btn-round" color="info" onClick={(e) => this.zakaziPregled(e) }>
                                  Zakazi pregled/operaciju
                                  </Button>
                                  </div>
                                  <div class = 'col-md-3'>
                                  <Button block className="btn-round" color="info" onClick={(e) => this.finishCheckup(e) }>
                                  Završi pregled
                                  </Button>
                                  </div>
                                  </Row>
                              </div>
      </div>
    </div>
    <p></p>
    <div className="row" hidden={this.state.showKarton || !this.state.canAccessToMedicalRecord}>
   <div  className="col-md-12">
          <p className="text lead">Uspostavljene dijagnoze</p>
          <div className="box mt-0 mb-lg-0">
            <div className="table-responsive">
              <table className="table table-hover">
                <thead>
                  <tr>
                    <th className="text-primary font-weight-bold">Naziv</th>
                    <th className="text-primary font-weight-bold">Ustanovljeno</th>
                    <th className="text-primary font-weight-bold">Doktor</th>
                    <th className="text-primary font-weight-bold">Klinika</th>                                                       
                  </tr>
                </thead>
                <tbody>  
                {this.state.diagnoses.map(diagnose => (
                  <tr key={diagnose.doctorName + " " + diagnose.doctorSurname} data-key={diagnose.doctorName + " " + diagnose.doctorSurname}>
                      <td>{diagnose.name}</td>
                      <td>{diagnose.diagnostingDate[2] + "." + diagnose.diagnostingDate[1] + "." + diagnose.diagnostingDate[0]}</td> 
                      <td>{diagnose.doctorName + " " + diagnose.doctorSurname}</td>
                      <td>{diagnose.clinicName}</td>                        
                  </tr>
                  ))}
                  
                </tbody>
              </table>
            </div>
          </div>
        </div>    
  </div>
</div>

<section className="bar pt-0" hidden={this.state.hideDocSearch || !this.state.showProfile}>
     <div className="content"  >
             <div className="row">  
             <div className="col-sm-3 col-md-3">
                      <div className="form-group">
                        <label>Ime doktora</label>
                        <input type="text" className="form-control" value={this.state.pretragadIme} onChange = {event => this.setState({pretragadIme: event.target.value})}/>
                      </div>
                    </div>           
                                     
                <div className="col-sm-3 col-md-3">
                      <div className="form-group">
                        <label>Prezime doktora</label>
                        <input type="text" className="form-control" value={this.state.pretragadPrezime} onChange = {event => this.setState({pretragadPrezime: event.target.value})}/>
                      </div>
                    </div> 

                    <div className="col-sm-3">
                      <div className="form-group">
                          <label>Ocena</label>
                          <select className="form-control" value={this.state.pretragadOcena} onChange = {event => this.setState({pretragadOcena: event.target.value})} >
                              <option>1</option>
                              <option>2</option>
                              <option>3</option>
                              <option>4</option>
                              <option>5</option>
                              <option>6</option>
                              <option>7</option>
                              <option>8</option>
                              <option>9</option>
                              <option>10</option>
                          </select>                            
                      </div>
                    </div>                                             
                                         
                    <div className="col-sm-3">
                    <div className="form-group">
                    <label style={{color: "white"}}>gbfhtjry</label>
                      <Button onClick={this.srchDoctors} block className="btn-round form-control" color="info">Pretraži</Button>
                    </div> </div>
                    </div>                      
       </div>
</section>  

<section className="bar pt-0" hidden={this.state.hideFilter || !this.state.showProfile}>
        <div className="row">            
            <div  className="col-sm-3">
                <Input id = "filterOcjena" name="filter[0]" type="text" placeholder="filtriraj po oceni" value={this.state.filterOcena} onChange={event => this.setState({filterOcena: event.target.value })} />
            </div>
              {/*<div  className="col-sm-3">
              <Input name="filter[0]" type="text" value={this.state.filter[0]} onChange={event => this.setState({filter: [...this.state.filter, event.target.value]})} />
                          </div>  */}                     
          <div  className="col-md-3">
              <Button block className="btn-round" color="info" onClick={this.cancelSearch}>Poništi pretragu</Button>
          </div>
          <div  className="col-md-3">    
              <Button id="filterClick" block className="btn-round" color="info" onClick={this.clinicFilter}>Filtriraj rezultate</Button>
          </div>
        </div>
</section> 

<section className="bar pt-0" hidden={this.state.hideKlinike || !this.state.showProfile}>
        <div className="row">
   <div  className="col-md-12">
          <p className="text lead">Izbor klinika klinickog centra</p>
          <div className="box mt-0 mb-lg-0">
            <div className="table-responsive">
              <table id = "tableE2E" className="table table-hover">
                <thead>
                  <tr>
                    <th onClick={() => this.sortByNaziv()} className="text-primary font-weight-bold">Naziv klinike</th>
                    <th onClick={() => this.sortByCity()}  className="text-primary font-weight-bold">Grad</th>
                    <th onClick={() => this.sortByAdresa()}  className="text-primary font-weight-bold">Adresa klinike</th>
                    <th onClick={() => this.sortByOcjena()} className="text-primary font-weight-bold">Prosečna ocena</th>
                    {this.state.pretragaHappened && <th className="text-primary font-weight-bold">Cena pregleda</th>}
                    <th className="text-primary font-weight-bold">Zvanična stranica</th>                     
                    {this.state.pretragaHappened && <th className="text-primary font-weight-bold">Doktori</th> }                                 
                  </tr>
                </thead>
                <tbody>  
                {this.state.clinics.map(clinic => (
                  <tr key={clinic.id} data-key={clinic.id}>
                      <td>{clinic.name}</td>
                      <td>{clinic.city}</td>
                      <td>{clinic.address}</td>
                      <td>{clinic.rating}</td> 
                      {this.state.pretragaHappened && <td>{clinic.appPrice}</td>   }                      
                      <td><Button id="profilKlinike" color="info" size="sm" onClick={this.redirect}>PROFIL</Button></td>
                      {this.state.pretragaHappened && <td><Button color="info" size="sm" data-key={clinic.name} onClick={this.doctorsFromClinic}>DOKTORI</Button></td>}
                  </tr>
                  ))}
                  
                </tbody>
              </table>
            </div>
          </div>
        </div>    
  </div>
  </section> 

  <section className="bar pt-0" hidden={this.state.hideDokore}>
        <div className="row">
   <div  className="col-md-12">
          <p className="text lead">Doktori izabranog kliničkog centra</p>
          <div className="box mt-0 mb-lg-0">
            <div className="table-responsive">
              <table className="table table-hover">
                <thead>
                  <tr>
                    <th onClick={() => this.sortByNazivDoc()} className="text-primary font-weight-bold">Ime</th>
                    <th className="text-primary font-weight-bold">Prezime</th>
                    <th onClick={() => this.sortByOcjenaDoc()} className="text-primary font-weight-bold">Prosečna ocena</th>
                    <th className="text-primary font-weight-bold">Slobodni termini</th>                            
                  </tr>
                </thead>
                <tbody>  
                {this.state.doctors.map(doctor => (
                  <tr key={doctor.user.name} data-key={doctor.id}>
                      <td>{doctor.user.name}</td>
                      <td>{doctor.user.surname}</td>
                      <td>{doctor.rating}</td>
                      <td><Button color="info" onClick = {this.setSelected}>SLOBODNI TERMINI</Button></td>
                  </tr>
                  ))}
                  
                </tbody>
              </table>
            </div>
          </div>
        </div>    
  </div>
  </section>  

  <Modal  modalClassName="modal-register" isOpen={this.state.showTermin}>
<div className="modal-header no-border-header text-center">
        <button
          aria-label="Close"
          className="close"
          data-dismiss="modal"
          type="button"
          onClick={event => this.setState({showTermin:false})} 
        >
        <span aria-hidden={true}>×</span>
        </button>
        </div>
        <div className="modal-body"> 
        <h2 className="title mx-auto">Izaberite slobodan termin pregleda</h2>   
        <form> 
        <div className="row">
                  <div className="col-md-12">
                    <div className="form-group">
                      <label className="text-primary font-weight-bold">Datum</label>
                      <input value={this.state.pretragaDatum} type="text" className="form-control" disabled={true}/>
                    </div>
                  </div>
                  </div>
                  <div className="row">
                  <div className="col-md-12">
                    <div className="form-group">
                      <label className="text-primary font-weight-bold">Termini</label>
                      <select value = {this.state.selectedTermin} onChange={(event) => this.setState({selectedTermin: event.target.value})} className="form-control" >
                          {this.state.selectedDoctor!=null && this.state.selectedDoctor.availableCheckups[this.state.pretragaDatum].map(item => (
                              <option key={item} data-key={item}>
                                  {item}
                              </option>
                          ))}
                      </select>
                    </div>
                  </div>
                  </div>        
      <Button block className="btn-round" color="info" onClick={event => this.setState({showAppointment: true, showTermin:false})}>Zakaži</Button>
      </form>
</div>
</Modal> 

<Modal  modalClassName="modal-register" isOpen={this.state.showDetailsCheckup}>
<div className="modal-header no-border-header text-center">
        <button
          aria-label="Close"
          className="close"
          data-dismiss="modal"
          type="button"
          onClick={event => this.setState({showDetailsCheckup:false})} 
        >
        <span aria-hidden={true}>×</span>
        </button>
        </div>
        <div className="modal-body"> 
        <h2 className="title mx-auto">Izveštaj</h2>   
        <form> 
        <div className="row">
                  <div className="col-md-12">
                    <div className="form-group">
                      <label className="text-primary ">Sala: {this.state.sala}</label>
                    </div>
                  </div>

                  <div className="col-md-12">
                    <div className="form-group">
                      <label className="text-primary ">Dijagnoza: {this.state.infoDijagnoza}</label>
                    </div>
                  </div>
                  </div>
                  <div className="row">
                  <div className="col-md-12">
                    <div className="form-group">
                          <label className="text-primary">Recepti: {this.state.infoRecepti}</label>
                      {/*<select value = {this.state.infoRecepti} className="form-control" >
                          {this.state.infoRecepti.map(item => (
                              <option key={item} data-key={item}>
                                  {item}
                              </option>
                          ))}
                      </select>*/}
                    </div>
                  </div>
                  </div>   
                  <div className="row">
                  <div className="col-md-12">
                    <div className="form-group">
                      <label className="text-primary">Informacije:</label>
                          <Input type="textarea" value={this.state.infoInfo} disabled={role !== 'DOKTOR' || this.state.buttonText === 'Izmeni'} onChange={(e) => this.setState({infoInfo:e.target.value})}>{this.state.infoInfo}</Input>
                          <br></br>
                          <Button hidden={this.state.ulogovani.id !== this.state.selectedReport.checkUp.medicalWorker.user.id} block className="btn-round" color="info" onClick={() => this.editReport()}>
          {this.state.buttonText}
        </Button>       
                    </div>
                  </div>
                  </div>      
      
      </form>
</div>
</Modal> 

<Modal  modalClassName="modal-register" isOpen={this.state.showAppointment}>
<div className="modal-header no-border-header text-center">
        <button
          aria-label="Close"
          className="close"
          data-dismiss="modal"
          type="button"
          onClick={event => this.setState({showAppointment:false})} // na izlaz 
        >
        <span aria-hidden={true}>×</span>
        </button>
        </div>
        <div className="modal-body"> 
        <h3 className="title mx-auto">Detalji pregleda</h3>
          <div class="row">              
            <div className="col-md-12 mx-auto">
              <form>
              <div className="row">
                  <div className="col-md-12">
                    <div className="form-group">
                      <label className="text-primary font-weight-bold">Klinika</label>
                      <input value={this.state.selectedDoctor!= null && this.state.selectedDoctor.clinic.name} type="text" class="form-control" disabled={true}/>
                    </div>
                  </div>
                  </div>

                <div className="row">
                  <div className="col-md-6">
                    <div className="form-group">
                      <label className="text-primary font-weight-bold">Tip pregleda</label>
                      <input value={this.state.pretragaTip} type="text" class="form-control" disabled={true}/>
                    </div>
                  </div>
                  
                  <div className="col-md-6">
                    <div className="form-group">
                      <label className="text-primary font-weight-bold">Doktor</label>
                      <input type="text" class="form-control" value={this.state.selectedDoctor!= null && this.state.selectedDoctor.user.name + " " + this.state.selectedDoctor.user.surname} disabled={true}/>
                    </div>
                  </div>
                  </div>
                  
                  <div className="row">
                  <div className="col-md-6">
                    <div class="form-group">
                      <label className="text-primary font-weight-bold">Datum</label>
                      <input type="text" class="form-control" value={this.state.pretragaDatum} disabled={true}/>
                    </div>
                  </div>                  
                  
                  
                  <div className="col-md-6">
                    <div className="form-group">
                      <label className="text-primary font-weight-bold">Vreme</label>
                      <input type="text" class="form-control" value={this.state.selectedTermin + "h"} disabled={true}/>
                    </div>
                  </div>
                  </div>
                  
                                    
              </form>
            </div>
          </div> 
      <Button block className="btn-round" color="info" onClick={this.medicalRequest}>POTVRDI</Button>
      </div>

</Modal> 

  <Col className="ml-auto mr-auto" md="6"> 
  <Form onSubmit={this.editPatient} hidden={this.state.showProfile}>
                <FormGroup>
                  <label className="text-primary font-weight-bold">JBO</label>
                  <Input name="jbo" type="text" value={this.state.jbo} onChange={event => this.setState({jbo: event.target.value})} disabled={true}/>
                </FormGroup>
                <FormGroup>
                  <label className="text-primary font-weight-bold"> Email</label>
                  <Input name="email" type="text" value={this.state.email} onChange={event => this.setState({email: event.target.value})} disabled={true}/>
                <FormText color="danger">{this.state.emailErrorText}</FormText>
                </FormGroup>
                <FormGroup>
                  <label className="text-primary font-weight-bold">Ime</label>
                  <Input name="name" type="text" value={this.state.name} onChange={event => this.setState({name: event.target.value})} disabled={role === 'DOKTOR'}/>
                </FormGroup>
                <FormGroup>
                  <label className="text-primary font-weight-bold">Prezime</label>
                  <Input  name="surname" type="text" value={this.state.surname} onChange={event => this.setState({surname: event.target.value})} disabled={role === 'DOKTOR'}/>
                </FormGroup>                                   
                <FormGroup>
                  <label className="text-primary font-weight-bold">Telefon</label>
                  <Input  name="phoneNumber" type="text" value={this.state.phone} onChange={event => this.setState({phone: event.target.value})} disabled={role === 'DOKTOR'}/>
                </FormGroup>
                <FormGroup>
                  <label className="text-primary font-weight-bold">Adresa</label>
                  <Input  name="adress"  type="text" value={this.state.adress} onChange={event => this.setState({adress: event.target.value})} disabled={role === 'DOKTOR'}/>
                </FormGroup>
                <FormGroup>
                  <label className="text-primary font-weight-bold">Grad</label>
                  <Input  name="city"  type="text" value={this.state.city} onChange={event => this.setState({city: event.target.value})} disabled={role === 'DOKTOR'}/>
                </FormGroup>
                <FormGroup>
                  <label className="text-primary font-weight-bold">Država</label>
                  <Input  name="statee" type="text" value={this.state.statee} onChange={event => this.setState({statee: event.target.value})} disabled={role === 'DOKTOR'}/>
                </FormGroup>
                <p className="text-danger font-weight-bold" >{this.state.formErrorText}</p>  
                <Button block className="btn-round" color="info" onClick={event => this.setState({changePass: true})} hidden={role === 'DOKTOR'}>
                  Izmeni lozinku
                </Button>               
                <Button block className="btn-round" color="info" disabled={this.state.formValid} hidden={role === 'DOKTOR'}>
                  Izmeni podatke
                </Button>
                </Form>
                     
  </Col>
  <div className="col-sm-2" hidden={this.state.chooseTip}>
                      <div className="row">
                      <div className="form-group">                          
                          <select className="form-control" value={this.state.historyType} onChange = {(event) => this.setState({historyType: event.target.value})} >
                          
                              <option key="PREGLED" >
                                  PREGLED                                    
                              </option>
                              <option key="OPERACIJA">
                                  OPERACIJA                                   
                              </option>
                            </select>
                              </div>
                          <Button block className="btn-round" color="info" onClick={this.displayCheckups}>POTVRDI</Button>                           
                      </div>
                      </div>
                    
  <section className="bar pt-0" hidden={this.state.tableHistory}>
  
  <div className="row">
   <div  className="col-md-12">
          <p className="text lead">Predstojeći pregledi</p>
          <div className="box mt-0 mb-lg-0">
            <div className="table-responsive">
              <table className="table table-hover">
                <thead>
                  <tr>
                    <th className="text-primary font-weight-bold">Vrsta pregleda</th>
                    <th className="text-primary font-weight-bold">Datum i vreme</th>
                    <th className="text-primary font-weight-bold">Doktor</th>
                    <th className="text-primary font-weight-bold">Klinika</th> 
                                                                
                  </tr>
                </thead>
                <tbody>  
                {this.state.icomingCheckups.map(checkup => (
                  <tr key={checkup.id} data-key={checkup.id}>
                      <td>{checkup.checkUpType.name}</td>
                      <td>{checkup.date[2] + "." + checkup.date[1] + "." + checkup.date[0] + " , " + checkup.time + "h"}</td> 
                      <td>{checkup.medicalWorker.user.name + " " + checkup.medicalWorker.user.name}</td>
                      <td>{checkup.clinic.name}</td>  
                                        
                  </tr>
                  ))}
                  
                </tbody>
              </table>
            </div>
          </div>
        </div>    
  </div>
  <div className="row">
   <div  className="col-md-12">
          <p className="text lead">Istorija pregleda</p>
          <div className="box mt-0 mb-lg-0">
            <div className="table-responsive">
              <table className="table table-hover">
                <thead>
                  <tr>
                    <th className="text-primary font-weight-bold">Vrsta pregleda</th>
                    <th className="text-primary font-weight-bold">Datum i vreme</th>
                    <th className="text-primary font-weight-bold">Doktori</th>
                    <th className="text-primary font-weight-bold">Klinika</th> 
                    <th className="text-primary font-weight-bold">Detalji</th>  {/* za detalje dodati report*/}                                                      
                  </tr>
                </thead>
                <tbody>  
                {this.state.checkupsHistory.map(checkup => (
                  <tr key={checkup.id} data-key={checkup.id}>
                      <td>{checkup.checkUpType.name}</td>
                      <td>{checkup.date[2] + "." + checkup.date[1] + "." + checkup.date[0] + " , " + checkup.time + "h"}</td> 
                      <td>
                        <span>
                          <p style={{display: "inline-block"}}> {checkup.medicalWorker.user.name + " " + checkup.medicalWorker.user.surname} </p>
                          <OverlayTrigger style={{display: "inline-block" }}
                                          trigger="click"
                                          key={checkup.id}
                                          placement="bottom"
                                          overlay={
                                            <Popover id={`popover-positioned-${checkup.id}`}>          
                                              <Popover.Content>
                                              <StarRatings
                                              rating={this.state.ratingD}
                                              starRatedColor="orange"
                                              changeRating={this.changeRatingDoctor}
                                              numberOfStars={5}
                                              name={checkup.id}
                                              starHoverColor="yellow"
                                              starDimension="20px"
                                              starSpacing="5px"
                                            />
                                              </Popover.Content>
                                            </Popover>
                                          }
                                        >
                            <Button color="info" ><i className="fa fa-star" /></Button>
                          </OverlayTrigger>
                        </span>
                      </td>
                      <td><span>
                          <p style={{display: "inline-block"}}> {checkup.clinic.name}</p>
                          <OverlayTrigger style={{display: "inline-block" }}
                                          trigger="click"
                                          key={checkup.id}
                                          placement="bottom"
                                          overlay={
                                            <Popover id={`popover-positioned-${checkup.clinic.id}`}>          
                                              <Popover.Content>
                                              <StarRatings
                                              rating={this.state.ratingCl}
                                              starRatedColor="orange"
                                              changeRating={this.changeRatingClinic}
                                              numberOfStars={5}
                                              name={checkup.id}
                                              starHoverColor="yellow"
                                              starDimension="20px"
                                              starSpacing="5px"
                                            />
                                              </Popover.Content>
                                            </Popover>
                                          }
                                        >
                            <Button color="info" ><i className="fa fa-star" /></Button>
                          </OverlayTrigger>
                        </span>
                        </td>  
                      <td><Button color="info" onClick = { ()  => this.checkupInfo(checkup.id)}>info</Button></td>                    
                  
                      
                  
                  </tr>
                  ))}

         
                  
                </tbody>
              </table>

              
            </div>
          </div>
        </div>    
  </div>

</section>
<section className="bar pt-0" hidden = {this.state.hideOperacije}>

 
  <div className="row">
   <div  className="col-md-12">
          <p className="text lead">Predstojeće operacije</p>
          <div className="box mt-0 mb-lg-0">
            <div className="table-responsive">
              <table className="table table-hover">
                <thead>
                  <tr>
                    <th className="text-primary font-weight-bold">Vrsta operacije</th>
                    <th className="text-primary font-weight-bold">Datum i vreme</th>
                    <th className="text-primary font-weight-bold">Doktor</th>
                    <th className="text-primary font-weight-bold">Klinika</th>                                    
                  </tr>
                </thead>
                <tbody>  
                {this.state.incomingOperations.map(checkup => (
                  <tr key={checkup.id} data-key={checkup.id}>
                      <td>{checkup.checkUpType.name}</td>
                      <td>{checkup.date[2] + "." + checkup.date[1] + "." + checkup.date[0] + " , " + checkup.time + "h"}</td> 
                      <td>{checkup.medicalWorker.user.name + " " + checkup.medicalWorker.user.name}</td>
                      <td>{checkup.clinic.name}</td>  
                                          
                  </tr>
                  ))}
                  
                </tbody>
              </table>
            </div>
          </div>
        </div>    
  </div>

 
  <div className="row">
   <div  className="col-md-12">
          <p className="text lead">Istorija operacija</p>
          <div className="box mt-0 mb-lg-0">
            <div className="table-responsive">
              <table className="table table-hover">
                <thead>
                  <tr>
                    <th className="text-primary font-weight-bold">Vrsta operacije</th>
                    <th className="text-primary font-weight-bold">Datum i vreme</th>
                    <th className="text-primary font-weight-bold">Doktor</th>
                    <th className="text-primary font-weight-bold">Klinika</th>                                                                     
                  </tr>
                </thead>
                <tbody>  
                {this.state.operationsHistory.map(checkup => (
                  <tr key={checkup.id} data-key={checkup.id}>
                      <td>{checkup.checkUpType.name}</td>
                      <td>{checkup.date[2] + "." + checkup.date[1] + "." + checkup.date[0] + " , " + checkup.time + "h"}</td> 
                      <td>
                        <span>
                          <p style={{display: "inline-block"}}> {checkup.medicalWorker.user.name + " " + checkup.medicalWorker.user.surname} </p>
                          <OverlayTrigger style={{display: "inline-block" }}
                                          trigger="click"
                                          key={checkup.id}
                                          placement="bottom"
                                          overlay={
                                            <Popover id={`popover-positioned-${checkup.id}`}>          
                                              <Popover.Content>
                                              <StarRatings
                                              rating={this.state.ratingD}
                                              starRatedColor="orange"
                                              changeRating={this.changeRatingDoctor}
                                              numberOfStars={5}
                                              name={checkup.id}
                                              starHoverColor="yellow"
                                              starDimension="20px"
                                              starSpacing="5px"
                                            />
                                              </Popover.Content>
                                            </Popover>
                                          }
                                        >
                            <Button color="info" ><i className="fa fa-star" /></Button>
                          </OverlayTrigger>
                        </span>
                      </td>
                      <td><span>
                          <p style={{display: "inline-block"}}> {checkup.clinic.name}</p>
                          <OverlayTrigger style={{display: "inline-block" }}
                                          trigger="click"
                                          key={checkup.id}
                                          placement="bottom"
                                          overlay={
                                            <Popover id={`popover-positioned-${checkup.clinic.id}`}>          
                                              <Popover.Content>
                                              <StarRatings
                                              rating={this.state.ratingCl}
                                              starRatedColor="orange"
                                              changeRating={this.changeRatingClinic}
                                              numberOfStars={5}
                                              name={checkup.id}
                                              starHoverColor="yellow"
                                              starDimension="20px"
                                              starSpacing="5px"
                                            />
                                              </Popover.Content>
                                            </Popover>
                                          }
                                        >
                            <Button color="info" ><i className="fa fa-star" /></Button>
                          </OverlayTrigger>
                        </span>
                        </td>  
                                          
                  
                  </tr>
                  ))}

         
                  
                </tbody>
              </table>

              
            </div>
          </div>
        </div>    
  </div>

</section>



</Container>
    </div>
            
<NotificationContainer/>        
  </div>
  )};
}

export default withRouter(PatientPage);
