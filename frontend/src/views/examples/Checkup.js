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
import {NotificationContainer, NotificationManager} from 'react-notifications';
import "../../../node_modules/react-notifications/lib/notifications.css"
import "../../../node_modules/react-notifications/lib/Notifications.js"
// reactstrap components
import {
    Button,
    Form,
    FormGroup,
    Input,
    Modal,
    Container,
    Col,
    Label,
    Alert
} from "reactstrap";

// core components
import ExamplesNavbar from 'components/Navbars/ExamplesNavbar.js';
import ProfilePageHeader from 'components/Headers/ProfilePageHeader.js';
import DemoFooter from 'components/Footers/DemoFooter.js';


class Checkup extends Component {
    constructor(props) {
        super(props);
        this.state = {
            descVal: '',
            codebooks: [],
            tipPregleda:"PREGLED",
            messageTime:"",
            vrijeme:"",
            dataValidation:"",
            messageDate1:"",
            datumNovog:"",
            showMessage: true,
            showSale: false,
            rooms: [],
            allRooms: [],
            nazivSaleFiltriranje:"",
            datumRez:"",
            slobodniTermini: [],
            izabraniTermin: "",
            tipSobeFiltriranje:"PREGLED",
            showRoomAvailability: false,
            showZakazivanje: false,
            selectedDate: "",
            selectedRoom:"",
            selectedDoctors:[],
            doctors:[],
            temp2:[],
            message: "" ,
            showDoctors: false,    
            selectedDoctor: '',
            appointmentDoctor:'',
            checkup:{patient: {
              user:{
                name: 'Ime',
                surname: 'Prezime',
                id: 1
              },
            },
              medicalWorker: {
                user:{
                  name: 'Dok ime',
                  surname: 'Dok prz',
                }
              },
              clinic: {
                name: 'Naziv klinike'
              },
              date: '2020-01-01',
              time: '10',
              room:{
                number: 1,
                name: 'Soba'
              },
              checkUpType: "TIP"
            }  
        };
    }

    doc = document.documentElement.classList.remove("nav-open");

    componentDidMount()
    {
      const {id} = this.props.match.params;
      axios({
        method: 'get',
        url: 'http://localhost:8099/checkup/'+id,
      }).then((response)=>{
        console.log(response);
        this.setState({checkup:response.data})
      },(error)=>{
        console.log(error);
      });
    }

    descriptionValidation(e){
        this.setState({informations:e.target.value})
        let desc = e.target.value
        if (desc === undefined || desc === "")
            this.setState({descVal:"Opis je obavezno polje"})
        else
            this.setState({descVal:""})
    }

   

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

    handleOptionChangeDoctor(changeEvent) {
        let doc = this.state.temp2;
        let pom = parseInt(changeEvent.target.value, 10);
        if(doc.includes(pom) && pom !== this.state.checkup.medicalWorker.id){
            const items = doc.filter(item => item !== pom);
            this.setState({ temp2: items });
        }
        else{
            const items = doc.filter(item => item !== pom);
            items.push(parseInt(changeEvent.target.value))
            this.setState({temp2: items});
        }
    };

    handleOptionChangeDoctor2(doctor) {
      let doc = this.state.temp2;
      let pom = doctor.id;
      if(!doc.includes(this.state.checkup.medicalWorker.id)){
          doc = [];
          doc.push(pom);
          this.setState({ selectedDoctor: pom, temp2:doc, appointmentDoctor:doctor});
      }

  };

    loadRooms = () => {
        axios({
            method: 'get',
            url: 'http://localhost:8099/clinic/getRooms/' + this.state.checkup.id,
          }).then((response) => {
            console.log(response);
            let all = response.data;
            all.sort(function(a, b){let date1 = new Date(a.firstFreeDate); let date2 = new Date(b.firstFreeDate); return date1 - date2})
            this.setState({allRooms:all, rooms:all})
        }, (error) => {
            console.log(error);
          });
    }



    dateValidation2(e){
    
        let ddatum = e.target.value;
        let godina;
        let mjesec;
        let dan;
        godina = ddatum[0] + ddatum[1]+ddatum[2]+ddatum[3]
    
        mjesec = ddatum[5] + ddatum[6]
     
        dan = ddatum[8] + ddatum[9]
     
        let newDate = this.state.checkup.date;
        let date = parseInt(newDate[2])
        let month = parseInt(newDate[1]);
        let year = parseInt(newDate[0]);
        godina = parseInt(godina)
        mjesec = parseInt(mjesec)
        dan = parseInt(dan)
       
       
      if(ddatum=== undefined || ddatum === ''){
        this.setState({messageDate1:"Datum ne moze biti raniji od prvobitno izabranog od strane doktora."})
      }
        else if(godina < year){
          this.setState({messageDate1:"Datum ne moze biti raniji od prvobitno izabranog od strane doktora."})
        }
        else if(mjesec< month && godina<=year){
          this.setState({messageDate1:"Datum ne moze biti raniji od prvobitno izabranog od strane doktora."})
        }
        else if(dan<date && mjesec<=month && godina<=year ){
          this.setState({messageDate1:"Datum ne moze biti raniji od prvobitno izabranog od strane doktora."})
        }
        else { 
          this.setState({messageDate:""})
          this.setState({messageDate1:""})
      }
        this.setState({datum:e.target.value})
      this.setState({datumRez:e.target.value})
    
      };

   logoutUser = () => {  
     localStorage.removeItem('ulogovan')
     localStorage.removeItem('role')
     this.props.history.push('/register-page');
   }

  sendEmailToDoctor = () => {
    axios({
      method: 'post',
      url: 'http://localhost:8099/notifyDoctor/' + this.state.checkup.id,
      ContentType: 'application/json',
    }).then((response)=>{      
     // alert("USPJESNO email doktoru");
     NotificationManager.success('Uspjesno ste obavijestili doktora!', 'Uspjesno!', 3000);
    },(error)=>{
     // alert("GRESKA doktro");
      console.log(error);
    });        
  }  

  sendEmailToPatient = () => {
    axios({
      method: 'post',
      url: 'http://localhost:8099/notifyPatient/' + this.state.checkup.id,
      ContentType: 'application/json',
    }).then((response)=>{      
     // alert("USPJESNO email pacijentu");
     NotificationManager.success('Uspjesno ste obavijestili pacijenta!', 'Uspjesno!', 3000);
    },(error)=>{
     // alert("GRESKA pacijent");
      console.log(error);
    });        
  }  

  sendEmailDateChanged = () => {
    axios({
      method: 'post',
      url: 'http://localhost:8099/changeDate/' + this.state.checkup.id,
      ContentType: 'application/json',
    }).then((response)=>{      
      //alert("USPJESNO datum promjenjen");
      NotificationManager.success('Uspjesan datum izmjena!', 'Uspjesno!', 3000);
    },(error)=>{
      //alert("GRESKA datum promjenjen");
      NotificationManager.error('Datum se ne moze promijeniti!', 'Greska!', 3000);

      console.log(error);
    });        
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

  redirect = () => {
    this.props.history.push('/register-page');
  }

  cancelSearchRoom = () => {
    this.setState({messageFilterSale:"", nazivSaleFiltriranje:"", tipSobeFiltriranje:this.state.checkup.type, rooms:this.state.allRooms, })
  }

  chooseRoom(room){
      let datum = room.firstFreeDate;
      let dan = datum[2];
      if (dan < 10){
          dan = '0' + dan;
      }
      let mjesec = datum[1];
      if(mjesec < 10){
          mjesec = '0'+mjesec;
      }
      //this.setState({showRoomAvailability:true});
      
      axios({
        method: 'get',
        url: 'http://localhost:8099/clinic/roomAvailability/' +  room.id + '/' + datum[0] + '-' + mjesec + '-' + dan,
      }).then((response)=>{       
        this.setState({slobodniTermini:response.data, izabraniTermin:response.data[0], selectedRoom:room})
        this.setState({showSale:false, selectedDate: datum[0] + '-' + datum[1] + '-' + datum[2]});
    this.loadDoctors();
      },(error)=>{
        console.log(error);
      });
  }

  loadDoctors(){
    let datum = this.state.selectedRoom.firstFreeDate;
    let dan = datum[2];
    if (dan < 10){
        dan = '0' + dan;
    }
    let mjesec = datum[1];
    if(mjesec < 10){
        mjesec = '0'+mjesec;
    }
    axios({
      method: 'get',
      url: 'http://localhost:8099/getAllAvailable/' + this.state.checkup.clinic.id + '/'+ datum[0] + '-' + mjesec + '-' + dan + '/' + this.state.izabraniTermin,
    }).then((response)=>{             
      this.setState({doctors:response.data})
      let doc = response.data;
      if(doc.some( d => d.id === this.state.checkup.medicalWorker.id )){
          let pom = [];
          pom.push(this.state.checkup.medicalWorker.id);
          this.setState({temp2: pom, selectedDoctors:pom});
          if(pom.includes(this.state.checkup.medicalWorker.id)){
            this.setState({selectedDoctor:this.state.checkup.medicalWorker.id, appointmentDoctor:this.state.checkup.medicalWorker});
          }
          else{
            this.setState({selectedDoctor:pom[0]})
          }
      }
    },(error)=>{
      console.log(error);
    });
  }
  
  choose = () =>{
    let datum = this.state.selectedRoom.firstFreeDate;
    this.setState({showRoomAvailability: false, showSale:false, selectedDate: datum[0] + '-' + datum[1] + '-' + datum[2]});
    this.loadDoctors();
    }

  
    update = () =>{
      let worker = this.state.appointmentDoctor;
      if(this.state.checkup.type === 'OPERACIJA'){
        worker = null;
      }
      let data = {
        "id" : this.state.checkup.id,
        "date" : this.state.selectedRoom.firstFreeDate,
        "room" : this.state.selectedRoom,
        "time" : this.state.izabraniTermin,
        "medicalWorker" : worker
    };
  
      axios({
        method: 'post',
        url: 'http://localhost:8099/checkup/update',
        data:data,
        ContentType: 'application/json',
      }).then((response)=>{      
          if(this.state.checkup.type === 'OPERACIJA'){          
            this.addDoctors();
          }
          else{
            this.sendEmailToDoctor();
            this.props.history.push('/registration-request')
          }
      },(error)=>{
       // alert("GRESKA");
        console.log(error);
      });
    }
     
    reserve(){
        this.update();
      }
    
      
      addDoctors(){
          axios({
            method: 'post',
            url: 'http://localhost:8099/checkup/addDoctors/' + this.state.checkup.id,
            data:this.state.selectedDoctors,
            ContentType: 'application/json',
          }).then((response)=>{
            this.sendEmailToPatient(); 
            let a = this.state.selectedRoom.firstFreeDate;
          let b = this.state.checkup.date;
          if(this.state.checkup.type === 'OPERACIJA' && !(a[0] === b[0] && a[1] === b[1] && a[2] === b[2])){
            this.sendEmailDateChanged();
          }
            this.props.history.push('/registration-request')  
          },(error)=>{
           // alert("DOKTOR ZAUZET");
            console.log(error);
          }); 
          
      }
  

  roomSearch = event => {
    event.preventDefault();
    let AuthStr = 'Bearer '.concat(localStorage.getItem("ulogovan"));
    let pom = [];
    pom.push(this.state.nazivSaleFiltriranje);
    pom.push(this.state.tipSobeFiltriranje);

    let ok = true;
    let ddatum = this.state.datumRez
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

 if(ok){
   pom.push(this.state.datumRez)
    
      axios({
        method: 'post',
        url: 'http://localhost:8099/clinic/searchRooms' ,
        headers: { "Authorization": AuthStr } ,
        data:pom,
        ContentType: 'application/json',
      }).then((response)=>{      
        if(response.data.length === 0)
        this.setState({messageFilterSale: "Ne postoji trazena sala u klinici", rooms:[]})
    else {
        this.setState({rooms:[]})
        this.setState({messageFilterSale:"", rooms:response.data})
        }
   
      },(error)=>{
        console.log(error);
      });
  }
}

  render() {
    return (
      <>
        <ExamplesNavbar logoutEvent={this.logoutUser}
                        hideKalendar={true}
                        
                        hideNewWorker = {true}
                        hideNewQuick = {true}
                        hideReceipts = {true}
                        hideTypeAdmin = {true}
                        hideCodebookAdmin = {true}                        
                        hidePregledi = {true}
                        hidePatientKlinike = {true}
                        hideCheckupDoctor = {true}
                        hideRoomsAdmin = {true}
                        hideDocsAdmin = {true}
                        hideClinics = {true}
                        hideClinicInfoAdmin = {true}
                        hideAddNewClinic = {true}
                        hidePatientsDoc = {true}
                        hideVacation = {true}                        
                        sakrij = {true}
                        hideAllQuicksEvent = {true}                      
                        hideKarton = {true}
        />
                <ProfilePageHeader />
                <Alert hidden={this.state.showMessage}>{this.state.message}</Alert>
                <div className="section profile-content">
                    <Container>
                        <Col className="ml-auto mr-auto" md="10">
                            <br></br>
                            <h3>Pregled</h3>
                            <br></br>
                            <br></br>
                            

                            <Modal modalClassName="modal-register" isOpen={this.state.showRoomAvailability}>
                                <div className="modal-header no-border-header text-center">
                                    <button aria-label="Close" className="close" data-dismiss="modal" type="button" onClick={() => this.setState({ showRoomAvailability: false })}>
                                        <span aria-hidden={true}>×</span>
                                    </button>
                                    <h3 className="title mx-auto">Izaberite termin</h3>
                                </div>
                                <ul className="list-group" id = "chooseTimeE2E">
                                    {this.state.slobodniTermini.map(termin => (
                                    <li key={termin} className="list-group-item">
                                        <label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                        <Input name="type" type="radio" value={termin} checked={termin === this.state.izabraniTermin} onChange={() => this.setState({izabraniTermin:termin})}></Input>
                                        {termin}
                                        </label>
                                    </li>
                                    ))}
                                </ul>
                                <div className="modal-body">
                                    <Button block className="btn-round" color="info" onClick={(e) => this.choose()}>
                                        Sačuvaj termin
                                    </Button>
                                </div>
                            </Modal>

                            <Modal modalClassName="modal-register" isOpen={this.state.showDoctors}>
                                <div className="modal-header no-border-header text-center">
                                    <button aria-label="Close" className="close" data-dismiss="modal" type="button" onClick={() => this.setState({ showDoctors: false })}>
                                        <span aria-hidden={true}>×</span>
                                    </button>
                                    <h3 className="title mx-auto">Izaberite doktore</h3>
                                </div>
                                <div hidden = {this.state.checkup.type === 'PREGLED'}>
                                <ul className="list-group">
                                    {this.state.doctors.map(doctor => (
                                    <li key={doctor.id} className="list-group-item">
                                        <label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                        <Input name="doco" type="checkbox" value={doctor.id} checked={this.state.temp2.includes(doctor.id) || this.state.checkup.medicalWorker.id === doctor.id} onChange={(event) => this.handleOptionChangeDoctor(event)}></Input>
                                        {doctor.user.name} {doctor.user.surname} ({doctor.user.email}) - {doctor.type}
                                        </label>
                                    </li>
                                    ))}
                                </ul>
                                </div>
                                <div hidden = {this.state.checkup.type === 'OPERACIJA'}>
                                <ul className="list-group">
                                    {this.state.doctors.map(doctor => (
                                    <li key={doctor.id} className="list-group-item">
                                        <label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                        <Input name="doco" type="radio" value={doctor.id} checked={this.state.selectedDoctor === doctor.id} onChange={(event) => this.handleOptionChangeDoctor2(doctor)}></Input>
                                        {doctor.user.name} {doctor.user.surname} ({doctor.user.email}) - {doctor.type}
                                        </label>
                                    </li>
                                    ))}
                                </ul>
                                </div>
                                <div className="modal-body">
                                    <Button block className="btn-round" color="info" onClick={(e) => this.setState({showDoctors: false, selectedDoctors:this.state.temp2})}>
                                        Sačuvaj
                                    </Button>
                                </div>
                            </Modal>

                            <Form>
                                <FormGroup>
                                    <label>Klinika: {this.state.checkup.clinic.name}</label>
                                </FormGroup>
                                <FormGroup>
                                    <label>Doktor: {this.state.checkup.medicalWorker.user.name} {this.state.checkup.medicalWorker.user.surname}</label>
                                </FormGroup>
                                <FormGroup>
                                    <label>Pacijent: {this.state.checkup.patient.user.name} {this.state.checkup.patient.user.surname}</label>
                                </FormGroup>
                                <FormGroup>
                                    <label>Datum: {this.state.checkup.date[0]}-{this.state.checkup.date[1]}-{this.state.checkup.date[2]}</label>
                                </FormGroup>
                                <FormGroup>
                                    <label>Vreme: {this.state.checkup.time}</label>
                                </FormGroup>
                                <FormGroup>
                                    <label>Tip pregleda: {this.state.checkup.checkUpType.name}</label>
                                </FormGroup>
                                <Button id = "findRoomE2E" block className="btn-round" color="info" onClick={() => {let datum = this.state.checkup.date; this.setState({showZakazivanje:true,showSale:true, datumRez:datum[0] + '-' + datum[1] + '-' + datum[2], tipSobeFiltriranje:this.state.checkup.type}); this.loadRooms()}} hidden={this.state.showSale}>
                                    Pronađi salu
                                </Button>
                                <div hidden = {this.state.selectedDate === ""}>
                                    ______________________________________________________________________________________________________________________________________________________
                                        <Label>Datum: {this.state.selectedDate}</Label>
                                        <br></br>
                                        <Label>Vreme: {this.state.izabraniTermin}</Label>
                                        <br></br>
                                        <Label>Sala: {this.state.selectedRoom.name} - {this.state.selectedRoom.number}</Label>  
                                        <br></br>
                                        <div className = "col-md-4">
                                        <Button block className="btn-round" color="info" onClick={() => {let datum = this.state.checkup.date; this.setState({showZakazivanje:true,temp2:[], selectedDoctors:[],showSale:true, datumRez:datum[0] + '-' + datum[1] + '-' + datum[2], tipSobeFiltriranje:this.state.checkup.type}); this.loadRooms()}} hidden={this.state.showSale}>
                                            Promeni salu i termin
                                        </Button>
                                        </div>
                                        <Label>Doktor: {this.state.selectedDoctors.length}</Label>  
                                        <div className = 'col-md-4'>
                                        <Button block className="btn-round" color="info" onClick={() => this.setState({showDoctors:true})}>
                                            Izaberi doktore
                                        </Button>
                                        </div>
                                        <br>
                                        </br>
                                        <Button id = "bookRoomE2E" block className="btn-round" color="info" onClick={() => this.reserve()}>
                                            Rezerviši salu
                                        </Button>
                                    </div>  
                                <div hidden = {!this.state.showSale}>
                                    <br></br>
                                    <p>_________________________________________________________________________________________________________________________________________</p>
                                    <p className="text lead" hidden={this.state.checkup.type === "PREGLED"}>Pronađi salu za operaciju</p>
                                    <p className="text lead" hidden={this.state.checkup.type === "OPERACIJA"}>Pronađi salu za pregled</p>
                                    <section className="bar pt-0">
                                        <div className="row">            
                                            <div  className="col-sm-3">
                                                <Input name="filter[0]" type="text" placeholder="trazi po nazivu" value={this.state.nazivSaleFiltriranje} onChange={event => this.setState({nazivSaleFiltriranje: event.target.value })} />
                                            </div>
                                            <div  className="col-md-3">
                                                <Input name="datum" type="date" value={this.state.datumRez} onChange={(event) => this.dateValidation2(event)} />  
                                                <p style={{color:'red'}}>{this.state.messageDate1}</p>     
                                            </div>   
                                            <div  className="col-md-3">    
                                                <Button block className="btn-round" color="info" onClick={this.roomSearch}>Pretrazi </Button>
                                            </div>
                                            <div  className="col-md-3">
                                                <Button block className="btn-round" color="info" onClick={this.cancelSearchRoom}>Poništi pretragu</Button>
                                            </div>
                                            <br></br><br></br> 
                                        </div>
                                    </section>
                                    <p style={{color:'red'}}>{this.state.messageFilterSale}</p>
                                        <section className="bar pt-0">
                                            <div className="row">
                                                <div  className="col-md-12">
                                                    <div className="box mt-0 mb-lg-0">
                                                        <div className="table-responsive">
                                                            <table className="table table-hover" id = "chooseRoomE2E">
                                                                <thead>
                                                                    <tr>
                                                                        <th className="text-primary font-weight-bold">Sale</th>
                                                                        <th className="text-primary font-weight-bold">Broj</th>
                                                                        <th className="text-primary font-weight-bold">Tip</th>
                                                                        <th className="text-primary font-weight-bold">Prvi slobodan dan</th>   
                                                                        <th className="text-primary font-weight-bold"></th>   
                                                                    </tr>
                                                                </thead>
                                                                <tbody>  
                                                                    {this.state.rooms.map(sala => (
                                                                        <tr key={sala.name}>
                                                                            <td>{sala.name}</td>
                                                                            <td>{sala.number}</td>
                                                                            <td>{sala.typeRoom}</td>
                                                                            <td> {sala.firstFreeDate[0] + '-' + sala.firstFreeDate[1] + '-' + sala.firstFreeDate[2]} </td>        
                                                                            <td><Button block className="btn-round" color="info"
                                                                                    onClick={(e) => this.chooseRoom(sala)}>
                                                                                    Izaberi
                                                                                </Button>
                                                                            </td>        
                                                                        </tr>))}
                                                                </tbody>
                                                            </table>
                                                        </div>
                                                    </div>
                                                </div>    
                                            </div>
                                        </section>
                                        <div  className="col-sm-3">                                            
                                            <Button block className="btn-round" color="info" onClick={(e) => this.setState({showSale:false})}>
                                                Odustani
                                            </Button>
                                        </div>
                                    </div>
                            </Form>
                        </Col>

                    </Container>
                    <NotificationContainer/>
                </div>
                <DemoFooter />
            </>
        )
    };
}

export default Checkup;

