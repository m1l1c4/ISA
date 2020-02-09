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
import React, { Component } from "react";
import axios from 'axios';
 
// reactstrap components
import {Button, FormGroup, Input, Container,  Col, Form, Modal, Label, Table, Row} from "reactstrap";
import ExamplesNavbar from 'components/Navbars/ExamplesNavbar.js';
import ProfilePageHeader from 'components/Headers/ProfilePageHeader.js';
import { Calendar, momentLocalizer } from 'react-big-calendar'
import moment from 'moment'

import {NotificationContainer, NotificationManager} from 'react-notifications';
import "../../../node_modules/react-notifications/lib/notifications.css"
import "../../../node_modules/react-notifications/lib/Notifications.js"

const localizer = momentLocalizer(moment)

 
class AdministratorPage extends Component {
  constructor(props) {
    super(props);
    this.state = {
      changeData: false,
      changePass: false,
      temp: true,
      name: "",
      surname: "",
      email: "",
      clinic: "",
      name1: "",
      surname1: "",
      password: "",
      pass1: "",
      pass2: "",
      cijena:"",
      temp1: false,
      surnameText: "",
      nameText: "",
      message: "",
      newWorker: false,
      newAppointment:false,
      workerName: "",
      workerEmail: "",
      workerSurname: "",
      workerPhone: "",
      workerStartHr: undefined,
      workerEndHr: undefined,
      date: new Date(),
      workerType: "DOKTOR",
      workerNameVal: "",
      workerSurnameVal: "",
      workerEmailVal: "",
      workerPhoneVal: "",
      workerStartHrVal: "",
      workerEndHrVal: "",
      povratna:"",
      vrijeme:"",
      datum: new Date(),
      salaTrenutna:"",
      messageTime:"",
      messageDate:"",
      doktor:"",
      tipTrenutni:"",
      allDoctors:[],
      allRooms:[],
      allTypes:[],
      messageCijena:"",
      cijenaValidation:"",
      typesSearch: [],
      showProfile:true,
      tipSearch:"",
      showTypeSearchForm:true,
      filterNaziv:"",
      messageFilter:"",
      showEditType:false,
      prije:"",
      noviNaziv:"",
      noviNazivValidation:"",
      poslijePoruka:"",
      addTypeModal: false,
      noviTip:"",
      pomocna:"",
      cijenaTipa:"",
      prijeCijena:"",
      prikaz: false,
      doctorType:"",
      types:[],
      clinics: [],
      selectedClinic: "",
      selectDoctors:[],
      korisnik:"",
      showDoctorsForm: true,
      imeDoktora:"",
      messageDoktor:"",
      doktori: [],
      doktoriSearch:[],
      filtriranje:true,
      nazivSaleFiltriranje:"",
      nazivSobe:"",
      brojSobe:"",
      tipSobe:"PREGLED",
      tipSobeFiltriranje:"PREGLED",
      saleModal:true,
      showEditRoom:false,
      prelazNaziv:"",
      prelazBroj:"",
      dobavljeneSale:[],
      messageFilterSale:"",
      brojSaleFiltriranje:"",
      showFiltriranjeSala:true,
      noviTipSale:"PREGLED",
      datumRez: "",
      clicniCitava: {},
      events:[],
      event:{},
      sort1: true,
      sort2: true,
      sort3: true,
      sort4: true,
      sort5: true,
      sort6: true,
      sort7: true,
    };
 
    this.updateOneAdministrator = this.updateOneAdministrator.bind(this);
    this.setujAdmina = this.setujAdmina.bind(this);
    this.createAppointment = this.createAppointment.bind(this);
    this.cancelSearch = this.cancelSearch.bind(this) ;
    this.tipFilter = this.tipFilter.bind(this);
    this.editTypeFunction = this.editTypeFunction.bind(this);
    this.deleteType = this.deleteType.bind(this);
    this.addType = this.addType.bind(this);
    this.cancelSearchDoctor = this.cancelSearchDoctor.bind(this) ;
    this.traziDoktora = this.traziDoktora.bind(this);
    this.deleteDoctor = this.deleteDoctor.bind(this);
    this.ponistiFiltriranje = this.ponistiFiltriranje.bind(this);
    this.filtrirajSale = this.filtrirajSale.bind(this);
    this.addRoom = this.addRoom.bind(this);
    this.dodaj = this.dodaj.bind(this);
    this.deleteRoom = this.deleteRoom.bind(this);
    this.roomSearch = this.roomSearch.bind(this);
    this.cancelSearchRoom = this.cancelSearchRoom.bind(this);
    this.traziPoBroju = this.traziPoBroju.bind(this);
    this.changeRoom = this.changeRoom.bind(this);
  }
 
  doc = document.documentElement.classList.remove("nav-open");
 
  //React.useEffect(() => {
  //document.body.classList.add("landing-page");
  //return function cleanup() {
  //document.body.classList.remove("landing-page");
  // };
  // });
 
  changeRoom = event => {
    event.preventDefault();
    let ime = this.state.prelazNaziv;
    let ok = true;
 
    let sun = ime;
    if (sun === '') {
      this.setState({ nameTextP: "Ovo polje ne moze biti prazno" });
      ok = false;
    }
    else {
      this.setState({ nameTextP: "" });
    }
 
   
    if(ok){
     
       let token = localStorage.getItem("ulogovan")
        let AuthStr = 'Bearer '.concat(token);
        let data = {
        "name": this.state.prelazNaziv ,  
        "number": this.state.prelazBroj ,
        "typeRoom": this.state.noviTipSale
        };        
        axios({
          method: 'post',
          url: 'http://localhost:8099/clinic/changeRoom',
          data: data,
          headers: { "Authorization": AuthStr }  
        }).then((response) => {
            this.setState({showEditRoom: false});
            this.setState({nameTextP:""})
            NotificationManager.success('Uspjesna izmjena sale!', 'Uspjesno!', 3000);
 
              axios({
                method: 'get',
                url: 'http://localhost:8099/clinic/getRooms',
                headers: { "Authorization": AuthStr }  
              }).then((response) => {
                console.log(response);
                  this.setState({dobavljeneSale:response.data})
              }, (error) => {
                console.log(error);
              });
 
       
 
            }, (error) => {
              console.log(error);
              if(error.status === "ALREADY_REPORTED")
              alert("Ne moze se mijenjati, nije slobodna!")
        });
   
    }
  }
 
  addRoom = event => {
    event.preventDefault();
 
    let number = this.state.brojSobe;
    let name = this.state.nazivSobe;
    let ok = true;
   
    if (number === undefined || number === ''){
        this.setState({roomNumVal:"Broj sale je obavezno polje"})
        ok = false;
    }
    else if (number <= 0){
        this.setState({roomNumVal:"Broj sale mora biti pozitivan."})
        ok = false;
    }
    else
        this.setState({roomNumVal:""})
   
    if (name === undefined || name === ''){
        this.setState({roomNameVal:"Naziv sale je obavezno polje"})
        ok = false;
    }
    else
        this.setState({roomNameVal:""})
   
    if(ok){
   
       let token = localStorage.getItem("ulogovan")
        let AuthStr = 'Bearer '.concat(token);
        let data = {
        "name": this.state.nazivSobe ,  
        "number": this.state.brojSobe ,
        "typeRoom": this.state.tipSobe,
        };        
        axios({
          method: 'post',
          url: 'http://localhost:8099/clinic/addRoom',
          data: data,
          headers: { "Authorization": AuthStr }  
        }).then((response) => {
 
          if(response.status === 208){
            NotificationManager.error('Sala sa ovim brojem vec postoji', 'Greska!', 3000);
          }
          else {
            this.setState({newRoomShow: false});
            this.setState({roomVal:""})
            NotificationManager.success('Uspjesno dodavanje sale', 'Uspjesno!', 3000);
          }
              axios({
                method: 'get',
                url: 'http://localhost:8099/clinic/getRooms',
                headers: { "Authorization": AuthStr }  
              }).then((response) => {
                console.log(response);
                  this.setState({dobavljeneSale:response.data})
              }, (error) => {
                console.log(error);
              });
 
       
 
        }, (error) => {
          if(error.status === "ALREADY_REPORTED")
          alert("Sala sa ovim imenom ili brojem vec postoji")
        });
   
    }
  };
 
cijenaTipaValidacija(e) {
  this.setState({prijeCijena : e.target.value});
  let number = e.target.value;
  if (number === undefined || number === '')
      this.setState({messageType:"Cijena pregleda je obavezno polje"})
  else if (number <= 0 || number > 1000)
      this.setState({messageType:"Opseg cijene se krece od 1 do 1000"})
  else
      this.setState({messageType:""})
}
 
  cancelSearch = () => {
    this.setState({messageFilter:""})
    this.setState({filterNaziv:""})
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
    axios({
      method: 'get',
      url: 'http://localhost:8099/clinic/getAllTypes',
      headers: { "Authorization": AuthStr }  
    }).then((response) => {
      console.log(response);
        this.setState({typesSearch:response.data})
    }, (error) => {
      console.log(error);
    });
  }
 
  cancelSearchRoom = () => {
    this.setState({messageFilterSale:""})
    this.setState({nazivSaleFiltriranje:""})
    this.setState({tipSobeFiltriranje:"PREGLED"})
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
    axios({
      method: 'get',
      url: 'http://localhost:8099/clinic/getRooms',
      headers: { "Authorization": AuthStr }  
    }).then((response) => {
      console.log(response);
      var today = new Date();
    let ddatum = today.getFullYear() + '-' + (today.getMonth() + 1) + '-' + today.getDate();
        this.setState({dobavljeneSale:response.data, showFiltriranjeSala:true, datumRez: ddatum, brojSaleFiltriranje:''})
    }, (error) => {
      console.log(error);
   
    });
  }
 
  sortByNaziv(){
    let temp = this.state.dobavljeneSale;
    if(this.state.sort5){
      temp.sort(function(a,b){let ime1 = a.name; let ime2 = b.name; return ime2.localeCompare(ime1)})
    }
  else{
    temp.sort(function(a,b){let ime1 = a.name; let ime2 = b.name; return ime1.localeCompare(ime2)})
  }
    this.setState({dobavljeneSale: []}) ;
    this.setState({dobavljeneSale:temp,sort5: !this.state.sort5})
  }

  sortByTip(){
    let temp = this.state.dobavljeneSale;
    if(this.state.sort6){
      temp.sort(function(a,b){let ime1 = a.typeRoom; let ime2 = b.typeRoom; return ime2.localeCompare(ime1)})
    }
  else{
    temp.sort(function(a,b){let ime1 = a.typeRoom; let ime2 = b.typeRoom; return ime1.localeCompare(ime2)})
  }
    this.setState({dobavljeneSale: []}) ;
    this.setState({dobavljeneSale:temp,sort6: !this.state.sort6})
  }


  sortByBroj(){
    let temp = this.state.dobavljeneSale;
    if(this.state.sort6){
      temp.sort(function(a,b){let ime1 = a.number; let ime2 = b.number; return ime2-ime1})
    }
  else{
    temp.sort(function(a,b){let ime1 = a.number; let ime2 = b.number; return ime1-ime2})
  }
    this.setState({dobavljeneSale: []}) ;
    this.setState({dobavljeneSale:temp,sort6: !this.state.sort6})
  }

  sortByIme(){
    let temp = this.state.doktori;
    if(this.state.sort1){
      temp.sort(function(a,b){let ime1 = a.user.name; let ime2 = b.user.name; return ime2.localeCompare(ime1)})
    }
  else{
    temp.sort(function(a,b){let ime1 = a.user.name; let ime2 = b.user.name; return ime1.localeCompare(ime2)})
  }
    this.setState({doktori:temp,sort1: !this.state.sort1})
  }

  sortByZvanje(){
    let temp = this.state.doktori;
    if(this.state.sort4){
      temp.sort(function(a,b){let z1 = a.user.type; let z2 = b.user.type; return z2.localeCompare(z1)})
    }
  else{
    temp.sort(function(a,b){let z1 = a.user.type; let z2 = b.user.type; return z1.localeCompare(z2)})
  }
    this.setState({doktori:temp,sort4: !this.state.sort4})
  }


  sortBySpecijalizacija(){
    let temp = this.state.doktori;
    if(this.state.sort3){
      temp.sort(function(a,b){let spec1 = a.type; let spec2 = b.type; return spec2.localeCompare(spec1)})
    }
  else{
    temp.sort(function(a,b){let spec1 = a.type; let spec2 = b.type; return spec1.localeCompare(spec2)})
  }
    this.setState({doktori:temp,sort3: !this.state.sort3})
  }

  sortByPrezime(){
    let temp = this.state.doktori;
    if(this.state.sort2){
      temp.sort(function(a,b){let prz1 = a.user.surname; let prz2 = b.user.surname; return prz2.localeCompare(prz1)})
    }
  else{
    temp.sort(function(a,b){let prz1 = a.user.surname; let prz2 = b.user.surname; return prz1.localeCompare(prz2)})
  }
    this.setState({pacijenti:temp,sort2: !this.state.sort2})
  }


  traziPoBroju = () => {
        const items = this.state.dobavljeneSale.filter(room => room.number === parseInt(this.state.brojSaleFiltriranje));
        this.setState({ dobavljeneSale: items });
        /*
        let AuthStr = 'Bearer '.concat(localStorage.getItem("ulogovan"));
        let pom = [];
        pom.push(this.state.brojSaleFiltriranje);
     
        axios({
          method: 'post',
          url: 'http://localhost:8099/clinic/filterRooms' ,
          headers: { "Authorization": AuthStr } ,
          data:pom,
          ContentType: 'application/json',
        }).then((response)=>{      
          if(response.status === 208)
          this.setState({messageFilterSale: "Ne postoji trazena sala u klinici"})
      else {
        let pom = [];
        this.setState({dobavljeneSale: []}) ;
        for(let i =0; i<response.data.length; i++){
              pom.push(response.data[i]);
        }
          this.setState({dobavljeneSale:pom}) ;
          this.setState({messageFilterSale:"", showFiltriranjeSala:false})
          }
     
        },(error)=>{
          console.log(error);
        });
        */
  }
 
  cancelSearchDoctor = () => {
    this.setState({messageDoktor:""})
    this.setState({imeDoktora:""})
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
    axios({
      method: 'get',
      url: 'http://localhost:8099/getAllDoctors',
      headers: { "Authorization": AuthStr }  
    }).then((response) => {
      console.log(response);
        this.setState({doktori:response.data, showFiltriranjeSala:true})
    }, (error) => {
      console.log(error);
    });
  }
 
  addType = event => {
    event.preventDefault();
   
    let name = this.state.noviTip;
    let ok = true;
   let number = this.state.cijenaTipa;
 
    if (number === undefined || number === ''){
      this.setState({messageType:"Cijena pregleda je obavezno polje"})
      ok = false;
    }
  else if (number <= 0 || number > 1000){
      this.setState({messageType:"Opseg cijene se krece od 1 do 1000"})
      ok = false;
  }
  else{
      this.setState({messageType:""})
  }
 
    for(let i= 0; i <this.state.typesSearch.length; i++){
      if(name === this.state.typesSearch[i].name){
        this.setState({messageType:"Naziv vec postoji!"})
        alert('Vec postoji')
        ok = false;
      }
    }
   
    if (name === undefined || name === ''){
        this.setState({messageType:"Naziv tipa je obavezno polje"})
        ok = false;
    }
    else
        this.setState({messageType:""})
   
       
   
    if(ok){
        let data = {
        "name": this.state.noviTip ,
        "typePrice": this.state.cijenaTipa,
        };        
        // ovo treba u response ok
        this.setState({temp1: false});
        this.setState({addTypeModal: false});
        this.setState({messageType:""})
        this.setState({noviTip:""})
   
        let token = localStorage.getItem("ulogovan")
        let AuthStr = 'Bearer '.concat(token);
   
        axios({
          method: 'post',
          url: 'http://localhost:8099/checkUpType/addType',
          data: data,
          headers: { "Authorization": AuthStr }  
        }).then((response) => {
          NotificationManager.success('Uspjesno dodavanje tipa!', 'Uspjesno!', 3000);
          console.log(response);
          axios({
            method: 'get',
            url: 'http://localhost:8099/clinic/getAllTypes',
            headers: { "Authorization": AuthStr }  
          }).then((response) => {
            console.log(response);
              this.setState({typesSearch:response.data, cijenaTipa:""})
          }, (error) => {
            console.log(error);
           
          });
 
         
        }, (error) => {
          console.log(error);
          if(error.status === "ALREADY_REPORTED")
          this.setState({message: "Tip pregleda sa ovim imenom u klinici vec postoji"})
        });
         
    }
  };
  
  
  showCalendar = (roomId) => {
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
 
    axios({
      method: 'get',
      url: 'http://localhost:8099/checkup/getCheckups/'+roomId,
      headers: { "Authorization": AuthStr },
    }).then((response)=>{             
      let checkups = response.data;
      let temp = [];
      for(let i= 0; i <checkups.length; i++){
        if(checkups[i].scheduled){
          let date = checkups[i].date;
          let time = Number(checkups[i].time) - 1;
          let endTime = Number(time) + 1;
          let color = 'deepskyblue';
          if(checkups[i].type === "PREGLED"){
            color = 'mediumseagreen';
          }
          let data = {
            id: checkups[i].id,
            patientId: checkups[i].patient.user.id,
            title: checkups[i].medicalWorker.user.name + ' ' + checkups[i].medicalWorker.user.surname,
            start: new Date(date[0], Number(date[1]) - 1, date[2], time, 0, 0),
            end: new Date(date[0], Number(date[1]) - 1, date[2], endTime, 0, 0),
            color: color,
            type: checkups[i].type
          }
          temp.push(data);
        }
      }
      this.setState({events: temp})
      this.setState({calendarModal:true})
    },(error)=>{
      console.log(error);
    });
  }


  addTypeNew = () => {
    this.setState({addTypeModal:true})
  }
 
  dodajNovog = () => {
    this.setState({newWorker:true});
  }
 
  dodaj = () =>{
    this.setState({newRoomShow:true});
  }
 
  changePassword = event => {
    event.preventDefault();
    let pom1 = this.state.password1;
    let pom2 = this.state.password11;
    let isOk = true;
    if (pom1 !== pom2) {
      this.setState({ message: "Lozinke se moraju poklapati!" });
      isOk = false;
    }
    else if (pom1 === undefined || pom1 === "") {
      this.setState({ message: "Lozinke se moraju poklapati!" });
      isOk = false;
    }
    else {
      this.setState({ changePass: false, password:this.state.password1, password1:"", password11:"" });
    }
 
    if (isOk) {
      let token = localStorage.getItem("ulogovan")
      let AuthStr = 'Bearer '.concat(token);
      let data = [];
      data.push(this.state.password1);
 
      axios({
        method: 'post',
        url: 'http://localhost:8099/changePassword',
        data: data,
        headers: { "Authorization": AuthStr } ,
        ContentType: 'application/json'
      }).then((response) => {
        NotificationManager.success('Uspjesna izmjena lozinke!', 'Uspjesno!', 3000);
        this.setState({ changeData: false })
      }, (error) => {
        console.log(error);
      });
 
      this.setState({ changeData: false })
    }
 
 
  }
 
    handleViewChange(e){
    let data = {
      title: "",
      date: "",
      timeStart: "",
      timeEnd: "",
      type: ""
    }
    this.setState({event: data})
  }

 handleClickEvent(e){
    let dateTime = String(e.start);
    dateTime = dateTime.split(" ");
    let date = dateTime[1] + ' ' + dateTime[2] + ' ' + dateTime[3];
    let time = dateTime[4];
    let temp = time.split(":");
    let time2 = (Number(temp[0])+1) + ':00:00';
    let data = {
      patientId: e.patientId,
      id: e.id,
      title: e.title,
      date: date,
      timeStart: time,
      timeEnd: time2,
      type: e.type
    }
    this.setState({event: data})
  }

  addDoctor = event =>{
    event.preventDefault();
  }
 
  setujAdmina(response) {
    this.setState({name:response.data.user.name, email:response.data.user.email,
      surname:response.data.user.surname,password:response.data.user.password,clinic:response.data.clinic.name}) ;
  }
 
  deleteType(name, e){
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
   
    axios({
      method: 'post',
      url: 'http://localhost:8099/checkUpType/deleteType/' + name,
      headers: { "Authorization": AuthStr }
    }).then((response) => {
      console.log(response);
      NotificationManager.success('Uspjesno brisanje tipa!', 'Uspjesno!', 3000);
      axios({
        method: 'get',
        url: 'http://localhost:8099/clinic/getAllTypes',
        headers: { "Authorization": AuthStr }  
      }).then((response) => {
        console.log(response);
          this.setState({typesSearch:response.data})
      }, (error) => {
        console.log(error);
        if(error.status === 'ALREADY_REPORTED'){
          this.setState({messageFilter:"Nije moguce izbrisati tip!"})
        }
      });
    }, (error) => {
      console.log(error);
    });
  };
 
  roomSearch = event => {
    event.preventDefault();
  let AuthStr = 'Bearer '.concat(localStorage.getItem("ulogovan"));
      let pom = [];
      pom.push(this.state.nazivSaleFiltriranje);
      pom.push(this.state.tipSobeFiltriranje);
 
 
      let ok = true;
    let ddatum = this.state.datumRez.split('-');
    let godina;
    let mjesec;
    let dan;
    godina = ddatum[0];
 
    mjesec = ddatum[1];
   
    dan = ddatum[2];
 
    let newDate = new Date();
    let date = parseInt(newDate.getDate())
    let month = parseInt(newDate.getMonth() + 1);
    let year = parseInt(newDate.getFullYear());
    godina = parseInt(godina)
    mjesec = parseInt(mjesec)
    dan = parseInt(dan)
   
   
  if(ddatum=== undefined || ddatum === ''){
    this.setState({messageDate1:"Datum je obavezno polje"})
    ok = false;
  }
    else if(godina < year){
      this.setState({messageDate1:"Godina je prosla"})
      ok = false;
    }
    else if(mjesec< month && godina<=year){
      this.setState({messageDate1:"Mjesec je prosao"})
      ok = false;
    }
    else if(dan<date && mjesec<=month && godina<=year ){
     
      this.setState({messageDate1:"Dan je prosao"})
      ok = false;
    }
    else { this.setState({messageDate1:""})
 
    this.setState({datum:ddatum})
    }
    let danPom = dan;
    let mjesecPom = mjesec;
    if(dan < 10){
      danPom = '0' + dan;
    }
    if(mjesec < 10){
      mjesecPom = '0' + mjesec;
    }
    var datumPom = godina + '-' + mjesecPom + '-' + danPom;
 if(ok){
   pom.push(datumPom)
 
      axios({
        method: 'post',
        url: 'http://localhost:8099/clinic/searchRooms' ,
        headers: { "Authorization": AuthStr } ,
        data:pom,
        ContentType: 'application/json',
      }).then((response)=>{      
        if(response.status === 208)
            this.setState({messageFilterSale: "Ne postoji trazeni sala u klinici"})
    else {
      let pom = [];
      this.setState({dobavljeneSale: []}) ;
      for(let i =0; i<response.data.length; i++){
            pom.push(response.data[i]);
      }
        this.setState({dobavljeneSale:pom}) ;
        this.setState({messageFilterSale:"", showFiltriranjeSala:false})
        }
   
      },(error)=>{
        console.log(error);
      });
  }
}
 
  deleteRoom(broj, e){
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
 
 
    axios({
      method: 'post',
      url: 'http://localhost:8099/clinic/deleteRoom/' + broj,
      headers: { "Authorization": AuthStr },
 
    }).then((response) => {
      console.log(response);
      if(response.status === 200){
        NotificationManager.success('Uspjesno brisanje sobe!', 'Uspjesno!', 3000);
        axios({
          method: 'get',
          url: 'http://localhost:8099/clinic/getRooms',
          headers: { "Authorization": AuthStr }  
        }).then((response) => {
          console.log(response);
            this.setState({dobavljeneSale:response.data})
        }, (error) => {
          console.log(error);
          NotificationManager.error('Nije moguce izbrisati salu!', 'Greska!', 3000);
          this.setState({messageFilterSale:"Nije moguce izbrisati salu!"})
        });
      }
      else if(response.status === 208){
        if(response.data === 'Greska'){
          alert("Nije moguce izbrisati salu!");
        }
      }
    }, (error) => {
      console.log(error);
    });
  };
 
 
  deleteDoctor(zaSlanje, e){
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
 
    let pom = {
      "email":zaSlanje,
    }
 
    axios({
      method: 'post',
      url: 'http://localhost:8099/deleteDoctor',
      headers: { "Authorization": AuthStr },
      data:pom,
      ContentType: 'application/json',
    }).then((response) => {
      console.log(response);
      NotificationManager.success('Uspjesno brisanje doktora!', 'Uspjesno!', 3000);
      let token1 = localStorage.getItem("ulogovan")
      let AuthStr1 = 'Bearer '.concat(token1);
      axios({
        method: 'get',
        url: 'http://localhost:8099/getAllDoctors',
        headers: { "Authorization": AuthStr1 }  
      }).then((response) => {
        console.log(response);
          this.setState({doktori:response.data})
      }, (error) => {
        console.log(error);
      });
   
 
    }, (error) => {
      console.log(error);
      NotificationManager.error('Nije moguce izbrisati doktora!', 'Greska!', 3000);
      this.setState({messageDoktor:"Nije moguce izbrisati doktora"})
    });
  };
 
  editTypeFunction = event => {
    event.preventDefault();
   
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
    let ok = true;
    let before = this.state.prije;
    let now = this.state.pomocna;
    let number = this.state.prijeCijena;
 
    if (number === undefined || number === ''){
      this.setState({poslijePoruka:"Cijena pregleda je obavezno polje"})
      ok = false;
    }
  else if (number <= 0 || number > 1000){
      this.setState({poslijePoruka:"Opseg cijene se krece od 1 do 1000"})
      ok = false;
  }
  else{
      this.setState({poslijePoruka:""})
  }
   
 
    let sun = this.state.pomocna;
   
    if(sun === '' || sun === undefined){
      this.setState({poslijePoruka:"Polje ne smije biti prazno!"})
      ok = false;
    }
    else{
      this.setState({poslijePoruka:""})
      ok = true;
    }
   
    let params = [];  
    params.push(before)
    params.push(now)
   params.push(this.state.prijeCijena)
   
    if(ok){
      axios({
        method: 'post',
        url: 'http://localhost:8099/clinic/changeNameOfType',
        headers: { "Authorization": AuthStr },
        data:params,
        ContentType: 'application/json',
      }).then((response) => {
        this.setState({poslijePoruka:"Nije moguce izmijeniti tip. (Postoji vec ili ima zakazan pregled tog tipa)"})
        NotificationManager.error('Izmjena nije moguca', 'Greska!', 3000);
        this.setState({pomocna:"",prije:"",poslijePoruka:"", prijeCijena:"",showEditType:false})
        console.log(response);
        axios({
          method: 'get',
          url: 'http://localhost:8099/clinic/getAllTypes',
          headers: { "Authorization": AuthStr }  
        }).then((response) => {
          console.log(response);
            this.setState({typesSearch:response.data})
        }, (error) => {
          console.log(error);
        });
      }, (error) => {
        console.log(error);
        if(error.status === "ALREADY_REPORTED"){
          this.setState({pomocna:"",prije:"",poslijePoruka:"", prijeCijena:"", showEditType:false})
         
        }
      });
    }
  }
 
  componentDidMount() {
    this.setState({showProfile:false});
    let token = localStorage.getItem("ulogovan");
    let AuthStr = 'Bearer '.concat(token);
    var today = new Date();
    let ddatum = today.getFullYear() + '-' + (today.getMonth() + 1) + '-' + today.getDate();
    this.setState({datum:ddatum, date:ddatum, datumRez:ddatum})
    axios({
      method: 'get' ,    
      url: 'http://localhost:8099/getUser' ,          
      headers: { "Authorization": AuthStr }  
      }).then((response) => {
        if (response.data != null)
        {
          this.setState({korisnik : response.data.type});
          if(response.data.type === 'CCADMIN'){
            this.setState({workerType:"ADMINISTRATOR", name:response.data.name, surname:response.data.surname, email:response.data.email});
         
            axios({
              method: 'get',
              url: 'http://localhost:8099/clinic/getClinics',
            }).then((response) => {
              console.log(response);
              this.setState({clinics:response.data})
              if(response.data.length > 0){
                this.setState({selectedClinic:response.data[0].name})
              }
            }, (error) => {
              console.log(error);
            });
          }
        }
        else{
          this.setState({korisnik : ""});
        }
      }, (error) => {
    });
 
   
 
    axios({
      method: 'get',
      url: 'http://localhost:8099/clinic/getAllTypes',
      headers: { "Authorization": AuthStr }  
    }).then((response) => {
      console.log(response);
        this.setState({allTypes:response.data})
        this.setState({typesSearch:response.data})
        if(this.state.allTypes.length > 0){
          this.setState({tipTrenutni:response.data[0].name, })
        }
    }, (error) => {
      console.log(error);
    });
 
    axios({
      method: 'get',
      url: 'http://localhost:8099/clinic/getClinic',
      headers: { "Authorization": AuthStr }  
    }).then((response) => {
      console.log(response);
        this.setState({clicniCitava: response.data});
        this.setState({clinic:response.data.name})
    }, (error) => {
      //console.log(error);
    });  
   
   
    axios({
      method: 'get',
      url: 'http://localhost:8099/clinic/getRooms',
      headers: { "Authorization": AuthStr }  
    }).then((response) => {
      console.log(response);
      const items = response.data.filter(room => room.typeRoom === 'PREGLED');
        this.setState({allRooms:items, dobavljeneSale:response.data})
        if(this.state.allRooms.length > 0){
          this.setState({salaTrenutna:items[0].number})
        }
    }, (error) => {
      console.log(error);
    });
   
    axios({
      method: 'get',
      url: 'http://localhost:8099/getAdministrator',
      headers: { "Authorization": AuthStr }
    }).then((response) => {
      console.log(response);
      this.setujAdmina(response);
    }, (error) => {
      console.log(error);
    });
  }
 
  handleOptionChange(changeEvent) {
    this.setState({
      workerType: changeEvent.target.value
    });
    if(changeEvent.target.value === 'MEDICINAR' || changeEvent.target.value === 'ADMINISTRATOR'){
      axios({
        method: 'get',
        url: 'http://localhost:8099/clinic/getClinics',
      }).then((response) => {
        console.log(response);
        this.setState({clinics:response.data})
        if(response.data.length > 0){
          this.setState({selectedClinic:response.data[0].name})
        }
      }, (error) => {
        console.log(error);
      });
    }
  };
 
  updateOneAdministrator = event => {
    event.preventDefault();
    let isOk = true;
 
    let nameMy = this.state.name1;
    let surnameMy = this.state.surname1;
 
 
    if (nameMy === undefined || nameMy === '') {
      this.setState({ nameValidation: "Ime je obavezno polje." })
      isOk = false;
    }
    else if (!nameMy[0].match('[A-Z]')) {
      this.setState({ nameValidation: "Ime mora pocinjati velikim slovom." })
      isOk = false;
    }
    else {
      this.setState({ nameValidation: "" })
    }
    if (surnameMy === undefined || surnameMy === '') {
      this.setState({ surnameValidation: "Prezime je obavezno polje." })
      isOk = false;
    }
    else if (!surnameMy[0].match('[A-Z]')) {
      this.setState({ surnameValidation: "Prezime mora pocinjati velikim slovom." })
      isOk = false;
    }
    else {
      this.setState({ surnameValidation: "" })
    }
 
    if (isOk) {
      let token = localStorage.getItem("ulogovan")
      let AuthStr = 'Bearer '.concat(token);
      this.setState({name:this.state.name1, surname:surnameMy})
      let data = {
        "user": {
          "name": nameMy,
          "surname": surnameMy,
          "email": this.state.email,
        },
 
        "clinic":{
          "name":this.state.clinic,
        },
      };
 
      axios({
        method: 'post',
        url: 'http://localhost:8099/updateAdministrator',
        data: data,
        headers: { "Authorization": AuthStr } ,
        ContentType: 'application/json'
      }).then((response) => {
        console.log(response);
        NotificationManager.success('Uspjesna izmjena podataka!', 'Uspjesno!', 3000);
        this.setState({ changeData: false })
      }, (error) => {
        console.log(error);
      });
 
      this.setState({ changeData: false })
    }
  }
 
  noviNazivValidation(e){
    let sun = e.target.value;
    this.setState({pomocna:e.target.value})
    if(sun === '' || sun === undefined){
      this.setState({poslijePoruka:"Polje ne smije biti prazno!"})
    }
    else{
      this.setState({poslijePoruka:""})
    }
  };
 
    editType(name, price, e){
      this.setState({prije:name, pomocna:name, prijeCijena:price, showEditType:true});
  };
 
  editRoom(name, number, e){
    this.setState({prelazNaziv:name, prijePromjene:number, prelazBroj:number, showEditRoom:true});
  };

  
 
  nameValidation(e) {
    this.setState({ name1: e.target.value });
    let sun = e.target.value;
    if (sun === '') {
      this.setState({ nameText: "Ovo polje ne moze biti prazno", flag1:false });
    }
    else if (!sun[0].match('[A-Z]')) {
      this.setState({ nameText: "Ime mora da pocinje velikim slovom", flag1:false });
    }
    else {
      this.setState({ nameText: "", flag1:true });
    }
 
  }
 
 
 prelazNameValidation(e) {
    this.setState({ prelazNaziv: e.target.value });
    let sun = e.target.value;
    if (sun === '') {
      this.setState({ nameTextP: "Ovo polje ne moze biti prazno" });
    }
    else {
      this.setState({ nameTextP: "" });
    }
  }
 
  surnameValidation(e) {
    this.setState({ surname1: e.target.value });
    let sun = e.target.value;
    if (sun === '') {
      this.setState({ surnameText: "Ovo polje ne moze biti prazno", flag2:false });
    }
    else if (!sun[0].match('[A-Z]')) {
      this.setState({ surnameText: "Prezime mora da pocinje velikim slovom", flag2:false });
    }
    else {
      this.setState({ surnameText: "", flag2:true });
    }
 
  }
 
  validacija2(e) {
    this.setState({ phone1: e.target.value });
    let sun = e.target.value;
    if (sun === '') {
      this.setState({ phoneValidation: "Ovo polje ne moze biti prazno", flag3:false });
    }
    else if (sun !== '') {
      this.setState({ phoneValidation: "", flag3:false });
    }
    else {
      this.setState({ phoneValidation: "", flag3:true });
    }
  }
 
 
  pass1Validation(e) {
    this.setState({ password1: e.target.value });
    let sun = e.target.value;
    if (sun === '') {
      this.setState({ password1Validation: "Ovo polje ne moze biti prazno" });
    }
    else if (sun !== '') {
      this.setState({ password1Validation: "" });
    }
    else {
      this.setState({ password1Validation: "" });
    }
  }
 
  pass2Validation(e) {
    let sun = e.target.value;
    this.setState({ password11: e.target.value });
    if (sun === '') {
      this.setState({ password11Validation: "Ovo polje ne moze biti prazno" });
    }
    else if (sun !== undefined && this.state.password1 !== e.target.value) {
      this.setState({ password11Validation: "Lozinke se moraju poklapati" })
    }
    else if (sun === this.state.password1) {
      this.setState({ password11Validation: "" })
    }
    else {
      this.setState({ password11Validation: "" });
    }
  }
 
  workerNameValidation(e) {
    this.setState({ workerName: e.target.value })
    let name = e.target.value;
    if (name === undefined || name === '')
      this.setState({ workerNameVal: "Ime radnika je obavezno polje." })
    else if (!name[0].match('[A-Z]'))
      this.setState({ workerNameVal: "Ime radnika mora pocinjati velikim slovom." })
    else
      this.setState({ workerNameVal: "" })
  };
 
  workerSurnameValidation(e) {
    this.setState({ workerSurname: e.target.value })
    let surname = e.target.value;
    if (surname === undefined || surname === '')
      this.setState({ workerSurnameVal: "Prezime radnika je obavezno polje." })
    else if (!surname[0].match('[A-Z]'))
      this.setState({ workerSurnameVal: "Prezime radnika mora pocinjati velikim slovom." })
    else
      this.setState({ workerSurnameVal: "" })
  };
 
  workerEmailValidation(e) {
    this.setState({ workerEmail: e.target.value })
    let email = e.target.value;
    const regex = /\S+@\S+\.\S+/;
    if (!regex.test(email))   //email not appropriate
    {
      this.setState({ workerEmailVal: "Neispravan mejl. Ocekivan unos je u obliku local@domain." })
    } else {
      this.setState({ workerEmailVal: "" })
    }
  };
 
  workerPhoneValidation(e) {
    this.setState({ workerPhone: e.target.value })
    let phone = e.target.value
    if (phone === undefined || phone === '')
      this.setState({ workerPhoneVal: "Telefon je obavezno polje" })
    else
      this.setState({ workerPhoneVal: "" })
  }
 
  workerStartHrValidation(e) {
    this.setState({ workerStartHr: e.target.value })
    let startHr = e.target.value
    if (startHr < 0 || startHr > 24)
      this.setState({ workerStartHrVal: "Radno vrijeme mora biti izmedju 00 i 24" })
    else
      this.setState({ workerStartHrVal: "" })
  }
 
  workerEndHrValidation(e) {
    this.setState({ workerEndHr: e.target.value })
    let endHr = e.target.value
    if (endHr < 0 || endHr > 24)
      this.setState({ workerEndHrVal: "Radno vrijeme mora biti izmedju 00 i 24" })
    else
      this.setState({ workerEndHrVal: "" })
  }
 
  doktorValidation(e){
    this.setState({doktor:e.target.value});
 
   
  }
   
  tipValidacija(e){
    this.setState({tipTrenutni:e.target.value});
    const items = this.state.allDoctors.filter(item => item.type === e.target.value);
    this.setState({selectDoctors:items});
    if(items.length > 0){
      this.setState({doktor:items[0].user.email})
    }
    else{
      this.setState({doktor:""})
    }
    /*
    axios({
      method: 'get',
      url: 'http://localhost:8099/clinic/getDoctors',
      headers: { "Authorization": AuthStr }  
    }).then((response) => {
      console.log(response);
       // this.setState({allDoctors:response.data})
        let pom = [];
        if(response.data.length > 0){
          for(let i=0; i<response.data.length; i++){
            if(response.data[i].type === this.state.tipTrenutni ){
              pom.push(response.data[i]);
            }
          }
          if(pom[0] != null){
          this.setState({doktor:pom[0].user.email})
            this.setState({allDoctors:pom})
          }
        }
    }, (error) => {
      console.log(error);
    });
    */
 
  }
  salaValidacija(e){
    this.setState({salaTrenutna:e.target.value});
  }
 
  tipDoktoraValidacija(e){
    this.setState({tipDoktora:e.target.value});
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
   
  cijenaValidation(e){
    this.setState({cijena:e.target.value})
    let number = e.target.value;
    if (number === undefined || number === '')
        this.setState({messageCijena:"Cijena pregleda je obavezno polje"})
    else if (number <= 0 || number > 1000)
        this.setState({messageCijena:"Opseg cijene se krece od 1 do 1000"})
    else
        this.setState({messageCijena:""})
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
    this.setState({datum:e.target.value})
  this.setState({datumRez:e.target.value})
 
  };
 
  createAppointment = event =>{
    event.preventDefault();
    let ok = true;
    let ddatum = this.state.datum
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
  }
    else if(godina < year){
      this.setState({messageDate:"Godina je prosla"})
    }
    else if(mjesec< month && godina<=year){
      this.setState({messageDate:"Mjesec je prosao"})
    }
    else if(dan<date && mjesec<=month && godina<=year ){
     
      this.setState({messageDate:"Dan je prosao"})
    }
    else  this.setState({messageDate:""})
 
    this.setState({datum:ddatum})
   
    let number = this.state.vrijeme;
    if (number === undefined || number === ''){
        this.setState({messageTime:"Vrijeme pregleda je obavezno polje"})
        ok = false;
    }
    else if (number < 8 || number > 18){
        this.setState({messageTime:"Radno vrijeme je od 8-18h"})
        ok = false;
    }
    else{
        this.setState({messageTime:""})
        ok = true;
    }
   
   let number1 = this.state.cijena;
    if (number1 === undefined || number1 === ''){
        this.setState({messageCijena:"Cijena pregleda je obavezno polje"})
        ok = false;
    }
    else if (number1 <= 0 || number1 > 1000){
        this.setState({messageCijena:"Opseg cijene se krece od 1 do 1000"})
        ok = false;
    }
    else{
        this.setState({messageCijena:""})
        ok = true;
    }  
   
    if (ok){
   
      let data = {
     
                  "checkUpType" :{
                      "name":this.state.tipTrenutni,
                  } ,
                  "price":this.state.cijena,
                  "date":this.state.datum,
                  "time":this.state.vrijeme,
                  "type":"PREGLED",
                  "room":{
                        "number" :this.state.salaTrenutna,
                    },
                  "medicalWorker":{
                          "user":{
                                "email" : this.state.doktor
                           },
                   },
         
         
      }
        let token = localStorage.getItem("ulogovan")
        let AuthStr = 'Bearer '.concat(token);
        axios({
          method: 'post',
          url: 'http://localhost:8099/checkup/addAppointment',
          data: data,
          headers: { "Authorization": AuthStr }  
        }).then((response) => {
          console.log(response);
          NotificationManager.success('Uspjesno dodavanje pregleda!', 'Uspjesno!', 3000);
         // alert('Uspjesno dodavanje pregleda!')
          this.setState({temp1: false});
        }, (error) => {
          console.log(error);
          if(error.status === "ALREADY_REPORTED")
          this.setState({povratna: "Vec postoji zakazan pregled u ovom terminu"})
          NotificationManager.error('Vec postoji zakazan pregled u ovom terminu!', 'Greska!', 3000);
        });
        this.setState({newAppointment:false})
      }
  }
 
  typeValidation(e){
    this.setState({noviTip:e.target.value})
    let name = e.target.value;
   
    for(let i = 0; i<this.state.typesSearch; i++){
       if(this.state.typesSearch.getItem(i).match(name)){
          this.setState({messageType:"Tip sa ovim nazivom vec postoji!"})
        }
    }
    if (name === undefined || name === '')
        this.setState({messageType:"Naziv tipa je obavezno polje."})
    else
        this.setState({messageType:""})
  };
   
 
  tipFilter = event => {
    event.preventDefault();
  let AuthStr = 'Bearer '.concat(localStorage.getItem("ulogovan"));
    let parametar = this.state.filterNaziv
    if (parametar === "")
      this.setState({messageFilter:"Polje za pretragu je prazno!"})
    else {

      axios({
        method: 'post',
        url: 'http://localhost:8099/clinic/searchOneType/' + parametar ,
        headers: { "Authorization": AuthStr } ,
        ContentType: 'application/json',
      }).then((response)=>{      
        this.setState({typesSearch: response.data}) ;
        this.setState({messageFilter:""})
      },(error)=>{
        console.log(error);
        this.setState({messageFilter: "Ne postoji trazeni tip pregleda u klinici"})
      });
  }
}
 
ponistiFiltriranje = event =>{
  event.preventDefault();
}
 
filtrirajSale = event =>{
  event.preventDefault();
}
 
roomNumberValidation(e){
  this.setState({brojSobe:e.target.value})
  let number = e.target.value;
  if (number === undefined || number === '')
      this.setState({roomNumVal:"Broj sale je obavezno polje"})
  else if (number <= 0)
      this.setState({roomNumVal:"Broj sale mora biti pozitivan."})
  else
      this.setState({roomNumVal:""})
};
 
roomNameValidation(e){
  this.setState({nazivSobe:e.target.value})
  let name = e.target.value;
  if (name === undefined || name === '')
      this.setState({roomNameVal:"Naziv sale je obavezno polje."})
  else
      this.setState({roomNameVal:""})
};
 
promjenaTipaSale(e){
  this.setState({ tipSobe:e.target.value});
}
 
promjenaTipaSaleChange(e){
  this.setState({noviTipSale:e.target.value})
}
 
promjenaTipaSaleF(e){
  this.setState({ tipSobeFiltriranje:e.target.value});
}
 
traziDoktora = event => {
  event.preventDefault();
let AuthStr = 'Bearer '.concat(localStorage.getItem("ulogovan"));
  let parametar = this.state.imeDoktora
  let tipD = this.state.tipDoktora;
 /* if (parametar === "")
    this.setState({messageDoktor:"Polje za pretragu je prazno!"})*/
 // else {
    let params = [];
    params.push(parametar);
    params.push(tipD);
 
    axios({
      method: 'post',
      url: 'http://localhost:8099/findDoctors',
      headers: { "Authorization": AuthStr } ,
      data: params,
      ContentType: 'application/json',
    }).then((response)=>{      
      this.setState({doktori: response.data}) ;
      this.setState({messageDoktor:""})
    },(error)=>{
      console.log(error);
      this.setState({messageDoktor: "Ne postoji doktor "})
    });
//}
}
 
  addMedicalWorker = event => {
    event.preventDefault();
    let ok = true;
    let name = this.state.workerName;
    let surname = this.state.workerSurname;
    let email = this.state.workerEmail;
    let phone = this.state.workerPhone;
    let startHr = this.state.workerStartHr;
    let endHr = this.state.workerEndHr;
    let nazivKlinike = "";
    if(this.state.workerType === 'MEDICINAR' || this.state.workerType === 'DOKTOR'){
      nazivKlinike = this.state.clinic;
    }
    else{
      nazivKlinike = this.state.selectedClinic;
    }
    let clinic = this.state.clinics.filter(item => item.name === nazivKlinike);
 
    if (name === undefined || name === '') {
      this.setState({ workerNameVal: "Ime je obavezno polje." })
      ok = false;
    }
    else if (!name[0].match('[A-Z0-9]')) {
      this.setState({ workerNameVal: "Ime mora pocinjati velikim slovom ili brojem." })
      ok = false;
    }
    else
      this.setState({ workerNameVal: "" })
 
    if (surname === undefined || surname === '') {
      this.setState({ workerSurnameVal: "Prezime je obavezno polje." })
      ok = false;
    }
    else if (!surname[0].match('[A-Z]')) {
      this.setState({ workerSurnameVal: "Prezime mora pocinjati velikim slovom." })
      ok = false;
    }
    else
      this.setState({ workerSurnameVal: "" })
 
    const regex = /\S+@\S+\.\S+/;
    if (!regex.test(email))   //email not appropriate
    {
      this.setState({ workerEmailVal: "Neispravan mejl. Ocekivan unos je u obliku local@domain." })
      ok = false;
    } else {
      this.setState({ workerEmailVal: "" })
    }
 
    if (this.state.workerType === 'DOKTOR' || this.state.workerType === "MEDICINAR") {
      if (phone === undefined || phone === '') {
        this.setState({ workerPhoneVal: "Telefon je obavezno polje" })
        ok = false;
      }
      else
        this.setState({ workerPhoneVal: "" })
 
      if (startHr < 0 || startHr > 24) {
        this.setState({ workerStartHrVal: "Radno vrijeme mora biti izmedju 00 i 24" })
        ok = false;
      }
      else
        this.setState({ workerStartHrVal: "" })
 
 
      if (endHr < 0 || endHr > 24) {
        this.setState({ workerEndHrVal: "Radno vrijeme mora biti izmedju 00 i 24" })
        ok = false;
      }
      else
        this.setState({ workerEndHrVal: "" })
    }
 
 
    if (ok) {
      if (this.state.workerType === 'DOKTOR' || this.state.workerType === "MEDICINAR") {
        let data = {
        "user": {
          "name": this.state.workerName,
          "surname": this.state.workerSurname,
          "email": this.state.workerEmail,
          "type": this.state.workerType
        },
        "phone": this.state.workerPhone,
        "startHr": this.state.workerStartHr,
        "endHr": this.state.workerEndHr,
        "type" : this.state.doctorType.name,
        "clinic" : this.state.clicniCitava,
        }
 
        axios({
          method: 'post',
          url: 'http://localhost:8099/addMedicalWorker',
          data: data,
          ContentType: 'application/json'
        }).then((response) => {
          console.log(response);
        //  alert("Uspjesno je dodat novi radnik")
        NotificationManager.success('Uspjesno je dodat novi radnik', 'Uspjeh!!', 3000);
          this.setState({ newWorker: false, workerName:"", workerSurname:"", workerEmail:"", workerType:"DOKTOR", workerPhone:"",
                        workerStartHr:"", workerEndHr:"",  });
                        let token = localStorage.getItem("ulogovan")
                        let AuthStr = 'Bearer '.concat(token);
                        axios({
                          method: 'get',
                          url: 'http://localhost:8099/getAllDoctors',
                          headers: { "Authorization": AuthStr }  
                        }).then((response) => {
                          console.log(response);
                            this.setState({doktori:response.data})
                        }, (error) => {
                          console.log(error);
                        });
        }, (error) => {
          console.log(error);
          NotificationManager.error('Email mora biti jedinstven', 'Greska!', 3000);
          //alert("Mejl mora biti jedinstven.")
        });
      }
      else{
        let data = {
          "user":{
            "name": this.state.workerName,
            "surname": this.state.workerSurname,
            "email": this.state.workerEmail,
            "type": this.state.workerType
          },
          "clinic" : clinic[0]
        }
          axios({
            method: 'post',
            url: 'http://localhost:8099/addAdmin',
            data: data,
            ContentType: 'application/json'
          }).then((response) => {
            console.log(response);
            NotificationManager.success('Uspjesno je dodat novi radnik', 'Uspjeh!!', 3000);
           // alert("Uspjesno je dodat novi radnik")
            this.setState({ newWorker: false, workerName:"", workerSurname:"", workerEmail:"", workerType:"ADMINISTRATOR"});
          }, (error) => {
            console.log(error);
            NotificationManager.error('Email mora biti jedinstven', 'Greska!', 3000);
           // alert("Mejl mora biti jedinstven.")
          });
      }
    }    
 
  };
 
  showCodebook(e){
    this.props.history.push('/codebook-page');
  }
 
  showRegistrationRequests(e){
    this.props.history.push('/registration-request');
  }

  showAddNewClinic(e){
    this.props.history.push('/clinic-page');

  }
 
  showClinicPage(e){
    /*if(this.state.korisnik === 'CCADMIN'){
      this.props.history.push('/clinic-page');
    }
    else{*/
      this.props.history.push('/viewandeditclinic-page');
    //}
  }
 
  noviPregled(e){
    this.setState({newAppointment:true});
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
    axios({
      method: 'get',
      url: 'http://localhost:8099/clinic/getAllTypes',
      headers: { "Authorization": AuthStr }  
    }).then((response) => {
      console.log(response);
        this.setState({allTypes:response.data})
        this.setState({typesSearch:response.data})
        if(this.state.allTypes.length > 0){
          this.setState({tipTrenutni:response.data[0].name, tipDoktora:response.data[0].name})
        }
    }, (error) => {
      console.log(error);
    });

    
    axios({
      method: 'get',
      url: 'http://localhost:8099/getAllDoctors',
      headers: { "Authorization": AuthStr }  
    }).then((response) => {
      console.log(response);
        let items = response.data.filter(doctor => doctor.type === this.state.tipTrenutni);
        this.setState({doktoriSearch:response.data, doktor:items[0].user.email})
        this.setState({doktori:response.data, allDoctors:response.data})
    }, (error) => {
      console.log(error);
    });
 

  }
 
  getAllCheckupTypes(e){
    this.setState({newWorker:true});
    if(this.state.korisnik === 'ADMINISTRATOR'){
      axios({
        method: 'get',
        url: 'http://localhost:8099/checkUpType/allTypes',
      }).then((response) => {
        console.log(response);
        this.setState({types: response.data});
        if(response.data.length > 0){
          this.setState({doctorType:response.data[0]});
          axios({
            method: 'get',
            url: 'http://localhost:8099/clinic/getClinicsByType/' + response.data[0].name,
          }).then((response) => {
            console.log(response);
            this.setState({clinics:response.data, selectedClinic:response.data[0].name})
          }, (error) => {
            console.log(error);
          });
        }
 
      }, (error) => {
        console.log(error);
      });
    }
  }
 
  setDoctorType(e){
    this.setState({doctorType: e.target.value});  
    axios({
      method: 'get',
      url: 'http://localhost:8099/clinic/getClinicsByType/' + e.target.value,
    }).then((response) => {
      console.log(response);
      this.setState({clinics:response.data, selectedClinic:response.data[0].name})
    }, (error) => {
      console.log(error);
    });
  }
  logoutUser = () => {  
    localStorage.removeItem('ulogovan')
    localStorage.removeItem('role')
    this.props.history.push('/register-page');
  }
 
  redirect = () => {
    this.props.history.push('/register-page');
  }
 
  showDoktori = (e) =>{
    this.setState({showDoctorsForm:false, showProfile:true, saleModal: true, showTypeSearchForm: true})
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
    axios({
      method: 'get',
      url: 'http://localhost:8099/getAllDoctors',
      headers: { "Authorization": AuthStr }  
    }).then((response) => {
      console.log(response);
        this.setState({doktori:response.data})
    }, (error) => {
      console.log(error);
    });
 
    axios({
      method: 'get',
      url: 'http://localhost:8099/clinic/getAllTypes',
      headers: { "Authorization": AuthStr }  
    }).then((response) => {
      console.log(response);
        this.setState({tipDoktora:response.data[0].name})
    }, (error) => {
      console.log(error);
    });
 
   
  }
 
  render() {
    return (
      <>
       
        <ExamplesNavbar logoutEvent={this.logoutUser}
                        saleShow={() => this.setState({saleModal: false, showProfile:true, showDoctorsForm:true, showTypeSearchForm: true})}
                        showTypeSearch = {()=> this.setState({showTypeSearchForm: false, showDoctorsForm:true, showProfile:true, saleModal: true})}
                        doktoriShow = {() => this.showDoktori()}
                        showProfileEvent={() => this.setState({showProfile: false, showTypeSearchForm:true, saleModal: true, showDoctorsForm:true})}
                        showNewAppointment={() => this.noviPregled()}
                        showNewWorker={() => this.getAllCheckupTypes()}
                        showViewAndEditPage={()=> this.showClinicPage()}
                        showCodebook={()=> this.showCodebook()}
                        showRegistrationRequests = {() => this.showRegistrationRequests()}
                        hideLoginEvent = {true}
                        hideAddNewClinic = {false}
                        hideClinicInfoAdmin = {false}
                        hideRegisterEvent = {true}
                        hideReceipts = {true}
                        hideTypeAdmin = {true}
                        hideCodebookAdmin = {false}
                        hideKalendar={true}
                        hidePregledi = {true}
                        hidePatientKlinike = {true}
                        hideCheckupDoctor = {true}                        
                        hideClinics = {true}                         
                        hidePatientsDoc = {true}
                        hideVacation = {true}                        
                        sakrij = {true}
                        hideAllQuicksEvent = {true}                      
                        hideKarton = {true}
                        showClinicPage = {() => this.showAddNewClinic() }
                        />
        <ProfilePageHeader />
        <div className="section profile-content">
          <Container>
            <div className="owner">
              <div className="avatar">
                <img
                  alt="..."
                  className="img-circle img-no-padding img-responsive"
                  src={require("assets/img/hc-complete-resize.jpg")}
                />
              </div>
              <div className="name">
                <h4 className="title">
                  {this.state.name} {this.state.surname}<br />
                </h4>
                <h6 className="description" hidden={this.state.korisnik === 'CCADMIN'}>Administrator klinike</h6>
                <h6 className="description" hidden={this.state.korisnik === 'ADMINISTRATOR'}>Administrator kliničkog centra</h6>
              </div>
            </div>
            <Row>
              <Col className="ml-auto mr-auto text-center" md="6">
                <p font size="3" color="red">
              </p>
                <br />
              </Col>
            </Row>
            <br />
 
            <div hidden={this.state.showDoctorsForm} >
<p className="text lead">Pretraga doktora u klinici</p>
        <section className="bar pt-0">
          <div className="row">            
              <div  className="col-sm-3">
                  <Input name="filter[0]" type="text" placeholder="trazi po imenu" value={this.state.imeDoktora} onChange={event => this.setState({imeDoktora: event.target.value })} />
              </div>
              <div  className="col-sm-3">
              <select value={this.state.tipDoktora}
          onChange={(e) =>  this.tipDoktoraValidacija(e)}    >
        {this.state.allTypes.map((tip) => <option key={tip.name} value={tip.name} >{tip.name}  </option>)}
      </select>
              </div>
                {/*<div  className="col-sm-3">
                <Input name="filter[0]" type="text" value={this.state.filter[0]} onChange={event => this.setState({filter: [...this.state.filter, event.target.value]})} />
                            </div>  */}                    
            <div  className="col-md-3">
                <Button block className="btn-round" color="info" onClick={this.cancelSearchDoctor}>Poništi pretragu</Button>
            </div>
            <div  className="col-md-3">    
                <Button block className="btn-round" color="info" onClick={this.traziDoktora}>Pretrazi </Button>
            </div>
           
          </div>
          <p style={{color:'red'}}>{this.state.messageDoktor}</p>
  </section>  
 
        <section className="bar pt-0">
          <div className="row">
     <div  className="col-md-12">
         <br></br> <br></br>
         <div  className="col-md-3">    
                <Button block className="btn-round" color="info" onClick={this.dodajNovog}>Dodaj novog radnika </Button>
            </div>
            <br></br>
             <div className="box mt-0 mb-lg-0">
              <div className="table-responsive">
                <table className="table table-hover">
                  <thead>
                    <tr>
                      <th onClick={() => this.sortByIme()} className="text-primary font-weight-bold">Ime</th>
                      <th onClick={() => this.sortByPrezime()} className="text-primary font-weight-bold">Prezime</th>

                      <th onClick={() => this.sortBySpecijalizacija()} className="text-primary font-weight-bold">Specijalizacija</th>
                      <th  onClick={() => this.sortByZvanje()} className="text-primary font-weight-bold">Zvanje</th>

                      {/*<th>Cena pregleda</th> */}              
                    </tr>
                  </thead>
                  <tbody>  
                  {this.state.doktori.map(dok => (
                    <tr key={dok.user.email}>
                        <td>{dok.user.name}  </td>
                        <td>{dok.user.surname} </td>
                        <td hidden = {dok.user.type === 'MEDICINAR'}>{dok.type}</td>
                        <td hidden = {dok.user.type === 'DOKTOR'}> </td>
                        <td>{dok.user.type}</td>


                  <td>      <Button block className="btn-round" color="info"  onClick={(e) => this.deleteDoctor(dok.user.email, e)}>Izbrisi radnika</Button> </td>
 
                    </tr>
                    ))}
                   
                  </tbody>
                </table>
              </div>
            </div>
          </div>    
    </div>
    </section>
    </div>
 
    <div hidden={this.state.saleModal} >
<p className="text lead">Pretraga sala u klinici</p>
        <section className="bar pt-0">
          <div className="row">            
              <div  className="col-sm-3">
                  <Input name="filter[0]" type="text" placeholder="trazi po nazivu" value={this.state.nazivSaleFiltriranje} onChange={event => this.setState({nazivSaleFiltriranje: event.target.value })} />
              </div>
                    <div  className="col-md-3">
                  <Input name="datum" type="date" value={this.state.datumRez} onChange={(event) => this.dateValidation(event)} />  
                  <p style={{color:'red'}}>{this.state.messageDate1}</p>    
                  </div>    
                  <div  className="col-sm-3">
                     <select  value={this.state.tipSobeFiltriranje}
                      onChange={(e) =>  this.promjenaTipaSaleF(e)}>
                      <option value={"PREGLED"}>Pregled</option>
                      <option value={"OPERACIJA"}>Operacija</option>
                    </select>
                  </div>        
          </div>
 
          <div className = "row">
          <div  className="col-md-3">    
                <Button block className="btn-round" color="info" onClick={this.roomSearch}>Pretrazi </Button>
            </div>
          <div  className="col-md-3">
                <Button block className="btn-round" color="info" onClick={this.cancelSearchRoom}>Poništi pretragu</Button>
            </div>
           
            <br></br><br></br>
             <div  className="col-md-3">
                <Button block className="btn-round" color="info" onClick={this.dodaj}>Dodaj salu</Button>
            </div>
          </div>
          <p style={{color:'red'}}>{this.state.messageFilterSale}</p>
  </section>  
 
 
 
  <div hidden={this.state.showFiltriranjeSala} >
  <section className="bar pt-0">
          <div className="row">            
              <div  className="col-sm-3">
                  <Input name="filter[0]" type="text" placeholder="trazi po broju" value={this.state.brojSaleFiltriranje} onChange={event => this.setState({brojSaleFiltriranje: event.target.value })} />
              </div>
                {/*<div  className="col-sm-3">
                <Input name="filter[0]" type="text" value={this.state.filter[0]} onChange={event => this.setState({filter: [...this.state.filter, event.target.value]})} />
                            </div>  */}              
            <div  className="col-md-3">    
                <Button block className="btn-round" color="info" onClick={this.traziPoBroju}>Trazi po broju  </Button>
            </div>
            <div  className="col-md-3">
                <Button block className="btn-round" color="info" onClick={this.cancelSearchRoom}>Poništi pretragu</Button>
            </div>
            <br></br><br></br>
          </div>
          <p style={{color:'red'}}>{this.state.messageFilterSale}</p>
  </section>  
                 
   
   </div>
 
        <section className="bar pt-0">
          <div className="row">
     <div  className="col-md-12">
         
            <div className="box mt-0 mb-lg-0">
              <div className="table-responsive">
                <table className="table table-hover">
                  <thead>
                    <tr>
                      <th onClick={() => this.sortByNaziv()} className="text-primary font-weight-bold">Naziv</th>
                      <th onClick={() => this.sortByBroj()} className="text-primary font-weight-bold">Broj</th>
                      <th onClick={() => this.sortByTip()}  className="text-primary font-weight-bold">Tip</th>
                      <th className="text-primary font-weight-bold">Prvi slobodan dan</th>
                      <th className="text-primary font-weight-bold">Kalendar zauzeca</th>
                    </tr>
                  </thead>
               
                  <tbody>  
                  {this.state.dobavljeneSale.map(sala => (
                    <tr key={sala.number}>
                        <td>{sala.name}</td>
                        <td>{sala.number}</td>
                        <td>{sala.typeRoom}</td>
                        <td> {sala.firstFreeDate[0]}-{sala.firstFreeDate[1]}-{sala.firstFreeDate[2]} </td>
                        <td>     <Button  block className="btn-round" color="info"  onClick={(e) => this.showCalendar(sala.id)}>Vidi kalendar</Button> </td>
                  <td>     <Button  block className="btn-round" color="info"  onClick={(e) => this.editRoom(sala.name, sala.number, e)}>Izmijeni salu</Button> </td>
                  <td>      <Button block className="btn-round" color="info"  onClick={(e) => this.deleteRoom(sala.number, e)}>Izbrisi salu</Button> </td>
               
                    </tr>
                    ))}
                   
                  </tbody>
                </table>
              </div>
            </div>
          </div>    
    </div>
    </section>
    </div>
 
            <Modal modalClassName="modal-register" isOpen={this.state.addTypeModal}>
<div className="modal-header no-border-header text-center">
          <button
            aria-label="Close"
            className="close"
            data-dismiss="modal"
            type="button"
            onClick={event => this.setState({addTypeModal:false} )}
          >
          <span aria-hidden={true}>x</span>
          </button>
          </div>
          <div className="modal-body">
          <Form onSubmit={this.addType} >
        <FormGroup>
        <label>Novi tip</label>
        <Input  name="tip"  value = {this.state.tipPregleda}  onChange={(event) => this.typeValidation(event)} type="text"  />
        <br></br>
        <label>Cijena pregleda ovog tipa</label>
        <Input  name="cijena"  value = {this.state.cijenaTipa}  onChange={(event) => {this.cijenaTipaValidacija(event); this.setState({cijenaTipa:event.target.value})}} type="number"  />
        <p style={{color:'red'}} > {this.state.messageType} </p>
        <p style={{color:'red'}} > {this.state.message1Type} </p>
        </FormGroup>
        <Button block className="btn-round" color="info"   >
       Dodaj
        </Button>
        </Form>
</div>
</Modal>



<Modal   style = {{maxWidth:"90vw"}} isOpen={this.state.calendarModal}>
<div className="modal-header no-border-header text-center" >
          <button
            aria-label="Close"
            className="close"
            data-dismiss="modal"
            type="button"
            onClick={event => this.setState({calendarModal:false} )}
          >
          <span aria-hidden={true}>x</span>
          </button>
          </div>
          <div  className="modal-body">
          <div>
            <Row>
                <Col md={{ span: 0 }} xs={12}>
                    <h3 align="center" className="border-bottom">Kalendar zauzeca sale</h3>
                </Col>
            </Row>
            <Row>
                <Col md={{offset: 1 }}  xs={9}>
                  <Calendar 
                    localizer={localizer}
                    events={this.state.events}
                    views={['day', 'week', 'month']}
                    popupOffset={{x: 30, y: 20}}
                    startAccessor="start"
                    endAccessor="end"
                    style={{height: 700}}
                    onSelectEvent = {(e) => this.handleClickEvent(e)}
                    eventPropGetter = {event => ({
                      style: {backgroundColor : event.color}
                    })}

                    onView = {(e) => this.handleViewChange(e)}
                  />
                </Col>
               
                <Col md={{ span: 0, offset : 0}} xs={2}>
                      <Row>&nbsp;</Row>
                      <Row>&nbsp;</Row>
                      <Row>&nbsp;</Row>

                      <Row>
                        <Table responsive>
                            <tbody>
                            <tr>
                                    <td  colspan = {2} align = "center"><h4>Detalji</h4></td>
                                </tr>
                                <tr>
                                    <th>Tip</th>
                                    <td align="left">{this.state.event.type}</td>
                                </tr>
                                <tr>
                                    <th>Datum</th>
                                    <td align="left">{this.state.event.date}</td>
                                </tr>
                                <tr>
                                    <th>Početak</th>
                                    <td align="left">{this.state.event.timeStart}</td>
                                </tr>
                                <tr>
                                    <th>Kraj</th>
                                    <td align="left">{this.state.event.timeEnd}</td>
                                </tr>
                            </tbody>
                        </Table>
                        </Row>
                    </Col>
            </Row>
        </div>

</div>
</Modal>
 
 
<Modal  modalClassName="modal-register" isOpen={this.state.showEditRoom}>
<div className="modal-header no-border-header text-center">
          <button
            aria-label="Close"
            className="close"
            data-dismiss="modal"
            type="button"
            onClick={event => this.setState({showEditRoom:false} )}
          >
          <span aria-hidden={true}>x</span>
          </button>
          </div>
          <div className="modal-body">
          <Form onSubmit={this.changeRoom} >
        <FormGroup>
        <label>Naziv sale</label>
        <Input  name="tip"  value = {this.state.prelazNaziv}  onChange={(event) => this.prelazNameValidation(event)} type="text"  />
        <p style={{color:'red'}} > {this.state.nameTextP} </p>
        <select value={this.state.noviTipSale}
                      onChange={(e) =>  this.promjenaTipaSaleChange(e)}>
                      <option value={"PREGLED"}>Pregled</option>
                      <option value={"OPERACIJA"}>Operacija</option>
                    </select>  
        <br></br>
     
        </FormGroup>
        <Button block className="btn-round" color="info"   >
       Sacuvaj
        </Button>
        </Form>
</div>
</Modal>
 
 
<Modal modalClassName="modal-register" isOpen={this.state.newRoomShow}>
     
      <div className="modal-header no-border-header text-center">
          <button
              aria-label="Close"
              className="close"
              data-dismiss="modal"
              type="button"
              onClick={() => this.setState({newRoomShow: false, roomNumVal:"", roomNameVal:""})}
          >
          <span aria-hidden={true}>×</span>
          </button>
          <h3 className="title mx-auto">Nova sala</h3>
      </div>
      <div className="modal-body">                      
              <Form onSubmit={this.addRoom}>
                  <FormGroup>
                  <label>Naziv</label>
                  <Input name="roomName" type="text" onChange={(event) => this.roomNameValidation(event)} />
                  <p style={{color:'red'}}>{this.state.roomNameVal}</p>
                  </FormGroup>
                  <FormGroup>
                  <label>Broj</label>
                  <Input name="roomNumber" type="number" onChange={(event) => this.roomNumberValidation(event)} />
                  <p style={{color:'red'}}>{this.state.roomNumVal}</p>
                  </FormGroup>      
                  <FormGroup>
                    <label> Tip sale &nbsp;&nbsp;&nbsp; </label>
                     <select value={this.state.tipSobe}
                      onChange={(e) =>  this.promjenaTipaSale(e)}>
                      <option value={"PREGLED"}>Pregled</option>
                      <option value={"OPERACIJA"}>Operacija</option>
                    </select>
                  </FormGroup>  
                  <Button block className="btn-round" color="info">
                       Dodaj salu
                  </Button>
               
                  </Form>
      </div>
      </Modal>
 
      <section className="bar pt-0" hidden={this.state.filtriranje}>
          <div className="row">            
              <div  className="col-sm-3">
                  <Input name="filter[0]" type="number" placeholder="filtriraj po broju" value={this.state.nazivSaleFiltriranje} onChange={event => this.setState({nazivSaleFiltriranje: event.target.value })} />
              </div>
                {/*<div  className="col-sm-3">
                <Input name="filter[0]" type="text" value={this.state.filter[0]} onChange={event => this.setState({filter: [...this.state.filter, event.target.value]})} />
                            </div>  */}                    
            <div  className="col-md-3">
                <Button block className="btn-round" color="info" onClick={this.ponistiFiltriranje}>Poništi filtriranje</Button>
            </div>
            <div  className="col-md-3">    
                <Button block className="btn-round" color="info" onClick={this.filtrirajSale}>Filtriraj rezultate</Button>
            </div>
          </div>
  </section>
 
<Modal modalClassName="modal-register" isOpen={this.state.showEditType}>
              <div className="modal-header no-border-header text-center">
                <button
                  aria-label="Close"
                  className="close"
                  data-dismiss="modal"
                  type="button"
                  onClick={event => this.setState({ showEditType: false, prije: ''})} // na izlaz
                >
                  <span aria-hidden={true}>x</span>
                </button>
              </div>
              <div className="modal-body">
                <Form onSubmit={this.editTypeFunction}>
                  <FormGroup>
                    <label>Naziv</label>
                    <Input name = "name" value={this.state.pomocna} onChange={(event) => this.noviNazivValidation(event)} type="text" />
                   
                    <label>Cijena pregleda ovog tipa</label>
        <Input  name="cijena"  value = {this.state.prijeCijena}  onChange={(event) => this.cijenaTipaValidacija(event)} type="number"  />
                    <p style={{ color: 'red' }} > {this.state.poslijePoruka} </p>
                  </FormGroup>
                  <Button block className="btn-round" color="info"    >
                    Sacuvaj
                  </Button>
                </Form>
              </div>
            </Modal>
 
           
 
            <Modal modalClassName="modal-login" isOpen={this.state.newAppointment}>      
        <div className="modal-header no-border-header text-center">
            <button
              aria-label="Close"
              className="close"
              data-dismiss="modal"
              type="button"
              onClick={() => this.setState({messageTime:"",messageCijena:"",messageDate:"", newAppointment:false,vrijeme:"",email:"",doktor:"",tipTrenutni:"",salaTrenutna:""})}
            >
            <span aria-hidden={true}>×</span>
            </button>
            <h3 className="title mx-auto">Kreiranje brzog pregleda</h3>
        </div>
        <div className="modal-body">                      
        <Form onSubmit={this.createAppointment}>
                  <FormGroup>
                  <label>Datum</label>
                  <Input name="datum" type="date" value={this.state.datum} onChange={(event) => this.dateValidation(event)} />  
                  <p style={{color:'red'}}>{this.state.messageDate}</p>    
                  </FormGroup>
                  <FormGroup>
                  <label>Vrijeme</label>
                  <Input name="vrijeme" value={this.state.vrijeme} type="number" onChange={(event) => this.timeValidation(event)} />
                  <p style={{color:'red'}}>{this.state.messageTime}</p>    
                  </FormGroup>
 
                  <FormGroup>
                  <label>Tip pregleda</label>
                  <div>
      <select value={this.state.tipTrenutni}
          onChange={(e) =>  this.tipValidacija(e)}    >
        {this.state.allTypes.map((tip) => <option key={tip.name} value={tip.name} >{tip.name}  </option>)}
      </select>
    </div>
    <div style={{color: 'red', marginTop: '5px'}}>
      </div>
                  </FormGroup>
 
<div hidden = {this.state.prikaz}>
                  <FormGroup>
                  <label>Doktor</label>
                  <div>
      <select value={this.state.doktor}
             onChange={(e) =>  this.doktorValidation(e)} >
        {this.state.allDoctors.map((dok) => <option key={dok.user.email} value={dok.user.email} hidden={dok.type !== this.state.tipTrenutni}>
          {dok.user.name} {dok.user.surname}  (  {dok.user.email}  )</option>)}
      </select>
    </div>
    <div style={{color: 'red', marginTop: '5px'}}>
        {this.state.validationError}
      </div>
                  </FormGroup>
                  </div>
             
 
                  <FormGroup>
                  <label>Sala</label>
                  <div>
      <select value={this.state.salaTrenutna}
          onChange={(e) =>  this.salaValidacija(e)}    >
        {this.state.allRooms.map((room) => <option key={room.name} value={room.number} >{room.name}  </option>)}
      </select>
    </div>
    <div style={{color: 'red', marginTop: '5px'}}>
      </div>
                  </FormGroup>
 
 
                  <FormGroup>
                  <label>Popust</label>  <b>  %</b>
                  <Input name="cijena" type="number" value={this.state.cijena} onChange={(event) => this.cijenaValidation(event)} />  
                 
                  <p style={{color:'red'}}>{this.state.messageCijena}</p>    
                  </FormGroup>
                  <p style={{color:'red'}}>{this.state.povratna}</p>
                  <Button block className="btn-round" color="info">
                       Dodaj pregled
                  </Button>
                  </Form>
        </div>
        </Modal>
 
 
            <Modal modalClassName="modal-register" isOpen={this.state.changePass}>
              <div className="modal-header no-border-header text-center">
                <button
                  aria-label="Close"
                  className="close"
                  data-dismiss="modal"
                  type="button"
                  onClick={event => this.setState({ changePass: false, password1: "", password11: "" })} // na izlaz
                >
                  <span aria-hidden={true}>x</span>
                </button>
              </div>
              <div className="modal-body">
                <Form onSubmit={this.changePassword} >
                  <FormGroup>
                    <label>Nova lozinka</label>
                    <Input name="password1" value={this.state.password1} onChange={(event) => this.pass1Validation(event)} type="password" />
                    <p style={{ color: 'red' }} > {this.state.password1Validation} </p>
                  </FormGroup>
                  <FormGroup>
                    <label>Potvrda lozinke</label>
                    <Input name="password11" value={this.state.password11} onChange={(event) => this.pass2Validation(event)} type="password" />
                    <label color="red" name="password11Validation" > </label>
                    <p style={{ color: 'red' }} > {this.state.password11Validation} </p>
                  </FormGroup>
                  <Button block className="btn-round" color="info"   >
                    Sacuvaj izmjene
        </Button>
                </Form>
              </div>
            </Modal>
 
            <div hidden={this.state.showTypeSearchForm} >
<p className="text lead">Pretraga tipa pregleda u klinici</p>
        <section className="bar pt-0">
          <div className="row">            
              <div  className="col-sm-3">
                  <Input name="filter[0]" type="text" placeholder="trazi po nazivu" value={this.state.filterNaziv} onChange={event => this.setState({filterNaziv: event.target.value })} />
              </div>
                {/*<div  className="col-sm-3">
                <Input name="filter[0]" type="text" value={this.state.filter[0]} onChange={event => this.setState({filter: [...this.state.filter, event.target.value]})} />
                            </div>  */}                    
            <div  className="col-md-3">
                <Button block className="btn-round" color="info" onClick={this.cancelSearch}>Poništi pretragu</Button>
            </div>
            <div  className="col-md-3">    
                <Button block className="btn-round" color="info" onClick={this.tipFilter}>Pretrazi </Button>
            </div>
             <div  className="col-md-3">
                <Button block className="btn-round" color="info" onClick={this.addTypeNew}>Dodaj tip</Button>
            </div>
          </div>
          <p style={{color:'red'}}>{this.state.messageFilter}</p>
  </section>  
 
        <section className="bar pt-0">
          <div className="row">
     <div  className="col-md-12">
         
            <div className="box mt-0 mb-lg-0">
              <div className="table-responsive">
                <table className="table table-hover">
                  <thead>
                    <tr>
                      <th className="text-primary font-weight-bold">Naziv tipa</th>
                   
                      {/*<th>Cena pregleda</th> */}              
                    </tr>
                  </thead>
                  <tbody>  
                  {this.state.typesSearch.map(tip => (
                    <tr key={tip.name}>
                        <td>{tip.name}</td>
                  <td>     <Button  block className="btn-round" color="info"  onClick={(e) => this.editType(tip.name, tip.typePrice, e)}>Izmijeni tip</Button> </td>
                  <td>      <Button block className="btn-round" color="info"  onClick={(e) => this.deleteType(tip.name, e)}>Izbrisi tip</Button> </td>
 
                    </tr>
                    ))}
                   
                  </tbody>
                </table>
              </div>
            </div>
          </div>    
    </div>
    </section>
    </div>
 
            <Modal modalClassName="modal-register" isOpen={this.state.changeData}>
              <div className="modal-header no-border-header text-center">
                <button
                  aria-label="Close"
                  className="close"
                  data-dismiss="modal"
                  type="button"
                  onClick={event => this.setState({ changeData: false, name1: '', surname1: '' })} // na izlaz
                >
                  <span aria-hidden={true}>x</span>
                </button>
              </div>
              <div className="modal-body">
                <Form onSubmit={this.updateOneAdministrator}>
                  <FormGroup>
                    <label>Ime</label>
                    <Input name="name" value={this.state.name1} onChange={(event) => this.nameValidation(event)} type="text" />
                    <p style={{ color: 'red' }} > {this.state.nameText} </p>
                  </FormGroup>
                  <FormGroup>
                    <label>Prezime</label>
                    <Input name="surname" value={this.state.surname1} onChange={(event) => this.surnameValidation(event)} type="text" />
                    <p style={{ color: 'red' }} > {this.state.surnameText} </p>
                  </FormGroup>
                  <p style={{ color: 'red' }} > {this.state.message} </p>
                  <Button block className="btn-round" color="info"    >
                    Sacuvaj
                  </Button>
                </Form>
              </div>
            </Modal>
 
            <Modal modalClassName="modal-register" isOpen={this.state.newWorker}>
              <div className="modal-header no-border-header text-center">
                <button
                  aria-label="Close"
                  className="close"
                  data-dismiss="modal"
                  type="button"
                  onClick={() => this.setState({ newWorker: false })}>
                  <span aria-hidden={true}>×</span>
                </button>
              </div>
              <div className="modal-body">
                <Form onSubmit={this.addMedicalWorker}>
                <FormGroup hidden={this.state.workerType === 'CCADMIN' || this.state.workerType === 'ADMINISTRATOR'}>
                  <label>Klinika: &nbsp;</label>
                  <Label name="clinic" type="text">{this.state.clinic}</Label>
                  </FormGroup>
                  <FormGroup>
                    <label>Uloga: &nbsp;&nbsp;&nbsp;</label>
                    <select value={this.state.workerType}
                      onChange={(e) =>  this.handleOptionChange(e)}>
                      <option value={"DOKTOR"} hidden={this.state.korisnik !== "ADMINISTRATOR"}>Doktor</option>
                      <option value={"MEDICINAR"} hidden={this.state.korisnik !== "ADMINISTRATOR"}>Medicinar</option>
                      <option value={"ADMINISTRATOR"} hidden={this.state.korisnik !== "CCADMIN"}>Administrator klinike</option>
                      <option value={"CCADMIN"} hidden={this.state.korisnik !== "CCADMIN"}>Administrator KC</option>
                    </select>
                  </FormGroup>
                  <FormGroup>
                    <label>Ime:</label>
                    <Input name="name" value={this.state.workerName} onChange={(event) => this.workerNameValidation(event)} type="text" />
                    <p style={{ color: 'red' }} > {this.state.workerNameVal} </p>
                  </FormGroup>
                  <FormGroup>
                    <label>Prezime:</label>
                    <Input name="surname" value={this.state.workerSurname} onChange={(event) => this.workerSurnameValidation(event)} type="text" />
                    <p style={{ color: 'red' }} > {this.state.workerSurnameVal} </p>
                  </FormGroup>
                  <FormGroup>
                    <label>Email:</label>
                    <Input name="email" value={this.state.workerEmail} onChange={(event) => this.workerEmailValidation(event)} type="text" />
                    <p style={{ color: 'red' }} > {this.state.workerEmailVal} </p>
                  </FormGroup>
                  <FormGroup hidden={this.state.workerType !== 'DOKTOR'}>
                    <label>Tip:</label>
                    <select className="form-control" value={this.state.doctorType} onChange = {(event) => this.setDoctorType(event)} >
                            {this.state.allTypes.map(type => (
                                <option key={type.name}>
                                    {type.name}                                    
                                </option>
                    ))}
                            </select>
                  </FormGroup>
                  <FormGroup hidden={this.state.workerType !== 'ADMINISTRATOR'}>
                    <label>Klinika:</label>
                    <select className="form-control" value={this.state.selectedClinic} onChange = {event => this.setState({selectedClinic:event.target.value})} >
                            {this.state.clinics.map(clinic => (
                                <option key={clinic.id}>
                                    {clinic.name}                                    
                                </option>
                    ))}
                            </select>
                  </FormGroup>
                  <div hidden={this.state.workerType === 'ADMINISTRATOR' || this.state.workerType === "CCADMIN"}>
                    <FormGroup>
                      <label>Telefon:</label>
                      <Input name="phone" value={this.state.workerPhone} onChange={(event) => this.workerPhoneValidation(event)} type="text" />
                      <p style={{ color: 'red' }} > {this.state.workerPhoneVal} </p>
                    </FormGroup>
                    <FormGroup>
                      <label>Pocetak radnog vremena:</label>
                      <Input name="starthr" value={this.state.workerStartHr} onChange={(event) => this.workerStartHrValidation(event)} type="number" />
                      <p style={{ color: 'red' }} > {this.state.workerStartHrVal} </p>
                    </FormGroup>
                    <FormGroup>
                      <label>Kraj radnog vremena:</label>
                      <Input name="endhr" value={this.state.workerEndHr} onChange={(event) => this.workerEndHrValidation(event)} type="number" />
                      <p style={{ color: 'red' }} > {this.state.workerEndHrVal} </p>
                    </FormGroup>
                  </div>
                  <Button block className="btn-round" color="info"    >
                    Sačuvaj
                  </Button>
                </Form>
              </div>
            </Modal>
 
            <Col className="ml-auto mr-auto" md="6">
              <Form hidden = {this.state.showProfile}>
                <FormGroup>
                  <label>Ime</label>
                  <Input name="name" disabled={this.state.temp} value={this.state.name} type="text" />
                </FormGroup>
                <FormGroup>
                  <label>Prezime</label>
                  <Input name="surname" disabled={this.state.temp} value={this.state.surname} type="text" />
                </FormGroup>
                <FormGroup>
                  <label>Email</label>
                  <Input name="email" disabled={this.state.temp} value={this.state.email} type="text" />
                </FormGroup>
                <FormGroup hidden={this.state.korisnik === 'CCADMIN'}>
                  <label>Klinika</label>
                  <Input name="clinic" disabled={this.state.temp} type="text" value={this.state.clinic} />
                </FormGroup>
                <Button block className="btn-round" color="info" onClick={event => this.setState({ changePass: true })}   >
                  Promijeni lozinku
                  </Button>
                <Button block className="btn-round" color="info" onClick={event => this.setState({ changeData: true, name1: this.state.name, surname1: this.state.surname })}    >
                  Izmijeni podatke
                  </Button>
              </Form>
            </Col>
          </Container>

          <NotificationContainer/>
        </div>
 
      </>
    )
  };
}
export default AdministratorPage;