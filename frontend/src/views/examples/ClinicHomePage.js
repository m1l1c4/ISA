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

// reactstrap components
import {
  Button,
  Label,
  FormGroup,
  Input,
  Container,
  Card, 
  CardBody, 
  Modal
} from "reactstrap";

import React , {Component} from "react";
import axios from 'axios';
import {NotificationContainer, NotificationManager} from 'react-notifications';
import "../../../node_modules/react-notifications/lib/notifications.css"
import "../../../node_modules/react-notifications/lib/Notifications.js"

//import "assets/css/bootstrap.min.css";
//import "assets/css/fontawesome.min.css";
import { withRouter } from "react-router-dom";
// core components
import ExamplesNavbar from 'components/Navbars/ExamplesNavbar.js';
import ProfilePageHeader from 'components/Headers/ProfilePageHeader.js';

class ClinicHomePage extends Component {
    constructor(props)
    {
      super(props);
      this.state = {         
        pretragaTip: "" ,  
        pretragaDatum: "" ,
        pretragadIme: "" ,  
        pretragadPrezime: "" ,
        pretragadOcena: 0 ,
        hiddenForm: false ,   // search form for clinics
        filterOcena: 0   ,   // 0 for rating, 1 for checkup price
        doctors: [] ,         
        selectedTermin: "" ,
        selectedDoctor: null ,
        showTermin: false ,
        showAppointment: false ,
        hideDocSearch: true , //hide form for searching doctors
        hideFilter: true ,        
        idClinic: -1 ,
        quicks: [] ,
        messageAlert: "" ,
        isVisible: false ,
        hideQuicks: true ,
        hideDocs: false ,
        types: [] ,
        clinic: null ,
        srchClicked: false ,
        valMessage: "",
        sort1: true,
        sort2: true,
        sort3: true,
        
      };
  
      this.getAllDoctors = this.getAllDoctors.bind(this) ;
      this.logoutUser = this.logoutUser.bind(this) ;
      this.redirect = this.redirect.bind(this) ;
      this.bookQuick = this.bookQuick.bind(this) ;
      this.onDismiss = this.onDismiss.bind(this) ;
      this.returnToProfile = this.returnToProfile.bind(this) ;
    }

  doc = document.documentElement.classList.remove("nav-open");
 
  componentDidMount()
  {
    const {id} = this.props.match.params ;
    this.setState({idClinic: id});
    this.getAllDoctors() ;
    this.getAllCheckupTypes() ;
    this.getClinicDetails() ;
  }

  getClinicDetails = () => {
    let id = this.state.idClinic; 
    let AuthStr = 'Bearer ' + localStorage.getItem("ulogovan"); 
    axios({
      method: 'get',
      url: 'http://localhost:8099/clinic/getDetails/' + id , 
      headers: { "Authorization": AuthStr } ,       
    }).then((response)=>{ 
      if (response.status !== 404)      
      this.setState({clinic: response.data}) ;
      
    },(error)=>{
      console.log(error);
    });
  }

  onDismiss(){
    this.setState({isVisible: false});
  } 

  sortByIme(){
    let temp = this.state.doctors;
    if(this.state.sort1){
      temp.sort(function(a,b){let ime1 = a.user.name; let ime2 = b.user.name; return ime2.localeCompare(ime1)})
    }
  else{
    temp.sort(function(a,b){let ime1 = a.user.name; let ime2 = b.user.name; return ime1.localeCompare(ime2)})
  }
    this.setState({doctors:temp,sort1: !this.state.sort1})
  }

  
  sortByOcjena(){
    let temp = this.state.doctors;
    if(this.state.sort3){
      temp.sort(function(a,b){let ime1 = a.rating; let ime2 = b.rating; return ime2-ime1})
    }
  else{
    temp.sort(function(a,b){let ime1 = a.rating; let ime2 = b.rating; return ime1-ime2})
  }
    this.setState({clinics:temp,sort3: !this.state.sort3})
  }

  
  sortBySpecijalizacija(){
    let temp = this.state.doctors;
    if(this.state.sort2){
      temp.sort(function(a,b){let spec1 = a.type; let spec2 = b.type; return spec2.localeCompare(spec1)})
    }
  else{
    temp.sort(function(a,b){let spec1 = a.type; let spec2 = b.type; return spec1.localeCompare(spec2)})
  }
    this.setState({doctors:temp,sort2: !this.state.sort2})
  }

  returnToProfile = () => {
    this.props.history.push('/patient-page');
  }

  bookQuick = (event) => {
    let quickId = event.target.parentElement.parentElement.getAttribute('data-key') 
    let AuthStr = 'Bearer '.concat(localStorage.getItem('ulogovan'))

    axios({
      method: 'post',
      url: 'http://localhost:8099/checkup/bookQuickApp/' + quickId , 
      headers: { "Authorization": AuthStr } ,       
    }).then((response)=>{ 
      if (response.status === 200) {
        this.getAllQuicks();     
        NotificationManager.success('Uspjesno je zakazan brzi pregled!', 'Uspjesno!', 3000);
        this.setState({isVisible: true }) ;
        // messageAlert: "Uspešno zakazan brzi pregled!"
      } else {
        this.getAllQuicks(); 
        this.setState({isVisible: true});
        NotificationManager.info('Izabrani pregled nije dostupan za zakazivanje!', 'Info!', 3000);
        // messageAlert: "Izabrani pregled nije dostupan za zakazivanje."
      }
      
      
    },(error)=>{
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

  getAllDoctors = () => {
    let param = this.state.idClinic
    axios({
      method: 'post',
      url: 'http://localhost:8099/clinic/allDocsOneClinic/' + param      
    }).then((response)=>{       
      this.setState({doctors: response.data, hideQuicks: true, hideDocs: false, hideDocSearch: true}) ;
      
    },(error)=>{
      console.log(error);
    });
  }

  getAllQuicks = () => {
    let param = this.state.idClinic
    axios({
      method: 'post',
      url: 'http://localhost:8099/checkup/getAllQuickApp/' + param      
    }).then((response)=>{  
      if (response.data !== null || response.data !== undefined || response.data !== [])   {
        this.setState({quicks: response.data, hideDocs: true, hideQuicks: false, hideDocSearch: true}) ;
      }  else {
        this.setState({messageAlert: "Trenutno nema unapred definisanih pregleda" , hideDocs: true, isVisible: true, hideDocSearch: true})
      }
      
      
    },(error)=>{
      console.log(error);
    });
  }

  getAllCheckupTypes = () => {
    let tipovi = []
    let i;
    let id = this.state.idClinic ;

   axios({
     method: 'get',
     url: 'http://localhost:8099/checkUpType/allTypesOneClinic/' + id ,      
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

  srchDoctors = () => {

    let parametri = []    
    parametri.push(this.state.clinic.name)
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
    this.setState({doctors: response.data, srchClicked: true}) ;
    
  },(error)=>{
    console.log(error);
  });

  }

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

  filterDoctors = (event) => {
    this.setState({pretragadIme: event.target.value})
    let name = event.target.value;
    let i, doktori;    
    let filteredD = [] ;
    name = name.toLowerCase();    

    for (i=0; i < this.state.doctors.length ; i++) {
      let str = this.state.doctors[i].user.name.toString();
      if (str.toLowerCase().includes(name)) {
          filteredD.push(this.state.doctors[i])
      }
    }

    if (filteredD.length !== 0) {
      if (localStorage.getItem("doktori") === undefined )
        {
          doktori = this.state.doctors ;
          localStorage.setItem("doktori", JSON.stringify(doktori));
        }
      
      this.setState({doctors: filteredD});
    }
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
      if (response.status === 200) {
        this.setState({ isVisible: true,showAppointment: false, selectedDoctor: null})
        NotificationManager.success('Uspjesno ste poslali zahtjev za zakazivanje pregleda!', 'Uspjeh!', 3000);
        // messageAlert: "Uspešno ste poslali zahtev za zakazivanje pregleda",
      }else {
        NotificationManager.error('Greska prilikom zakazivanja pregleda!', 'Greska!', 3000);
      }
      
      
    },(error)=>{
      alert("GRESKAAA")
      console.log(error);
    });
}

  filterDoctorsSur = (event) => {
    this.setState({pretragadPrezime: event.target.value})
    let name = event.target.value;    //prezime u stvari
    let i;    
    let filteredD = [] ;
    name = name.toLowerCase();    

    if (name === "" && this.state.pretragadIme === "") {
      let doktori = localStorage.getItem("doktori");
      doktori = JSON.parse(doktori)
      this.setState({doctors: doktori})
      return;
    }

    for (i=0; i < this.state.doctors.length ; i++) {
      let str = this.state.doctors[i].user.surname.toString();
      if (str.toLowerCase().includes(name)) {
          filteredD.push(this.state.doctors[i])
      }
    }

    if (filteredD.length !== 0) {
      let doktori = this.state.doctors
      if (localStorage.getItem("doktori") === undefined    )
        localStorage.setItem("doktori", JSON.stringify(doktori));
      
      this.setState({doctors: filteredD});
    }
  }

  switchSearchForm = () => {
    this.setState({hideDocSearch: !this.state.hideDocSearch})
  }

  cancelSearch = () => {
    this.setState({hideDocSearch: true})
    this.getAllDoctors();
  }

  validateDate = event => {
    let date = event.target.value ;
    let today = new Date() ;
    let z = "0" ;

    if ((today.getMonth()+1) >= 10) {
      z = "" ;
    }

    var currentDate = today.getFullYear()+'-'+ z + (today.getMonth()+1)+'-'+today.getDate();

    if (date < currentDate) {
      this.setState({valMessage: "Datum mora biti veći od današnjeg."})
    } else {
      this.setState({pretragaDatum: date, valMessage: ""})
    }

    
  }
  
  render() {
  return (
    <div>
  
  <>  
      <ExamplesNavbar logoutEvent={this.logoutUser} 
                      showDocs = {this.getAllDoctors}  
                      clickQuick = {this.getAllQuicks}                    
                      hideKalendar={true}
                      hideLoginEvent = {true}
                      hideNewWorker = {true}
                      hideNewQuick = {true}
                      hideReceipts = {true}
                      hideTypeAdmin = {true}
                      hideCodebookAdmin = {true}
                      hideRequestsAdmin = {true}
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
                      hideKarton = {true}
                      showProfileEvent = {this.returnToProfile}
                      />
                      
      <ProfilePageHeader /><br></br>      
      <div className="section profile-content">        
        <Container >

        <button onClick = {this.switchSearchForm} style={{background: "#2196F3", color: "white", float: "right"}}> <i class="fa fa-search"></i></button>

     <form hidden={this.state.hideDocSearch}>
      <div className="form-row">
          <FormGroup className="col-md-3">
            <Label>Tip pregleda</Label>
            <select className="form-control" value={this.state.pretragaTip} onChange = {event => this.setState({pretragaTip: event.target.value})} >
                            {this.state.types.map(type => (
                                <option key={type}>
                                    {type}                                    
                                </option>
                              ))} 
                            </select> 
          </FormGroup>
          <FormGroup className="col-md-3">
            <Label>Datum</Label>
            <Input type="date" value={this.state.pretragaDatum} onChange = {this.validateDate}/>
            <p style={{color:'red', fontSize: "12px"}}>{this.state.valMessage}</p>
          </FormGroup>
          <FormGroup className="col-md-2">
            <Label>Ocena</Label>
            <Input type="text" value={this.state.pretragaOcena} onChange = {event => this.setState({pretragaOcena: event.target.value})}/>
          </FormGroup>
          <FormGroup className="col-md-2" style={{textAlign: "center"}}>
          <Label  style={{color: "white"}} >dgrt</Label>
            <Button  onClick={this.srchDoctors} block className="btn-round form-control" color="info">Pretraži</Button>               
          </FormGroup>
          </div>

          <div className="form-row">
          <FormGroup className="col-md-3">
            <Label>Ime</Label>
            <Input type="text" value={this.state.pretragadIme} onChange = {this.filterDoctors}/>
          </FormGroup>
          <FormGroup className="col-md-4">
            <Label>Prezime</Label>
            <Input type="text" value={this.state.pretragadPrezime} onChange = {this.filterDoctorsSur}/>
          </FormGroup>
          <FormGroup className="col-md-2" style={{textAlign: "center"}}>
          <Label  style={{color: "white"}} >thy</Label>
            <Button  onClick={this.cancelSearch} block className="btn-round form-control" color="info">Poništi</Button>               
          </FormGroup>
          </div>

          
      
     </form> 

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












    <section className="bar pt-0" hidden={this.state.hideDocs}>
       <div className="row">
     <div  className="col-md-12">
            <p className="text lead">Zaposleni doktori</p>
            <div className="box mt-0 mb-lg-0">
              <div className="table-responsive">
                <table className="table table-hover">
                  <thead>
                    <tr>
                      <th  onClick={() => this.sortByIme()} className="text-primary font-weight-bold">Ime i prezime</th>
                      <th  onClick={() => this.sortBySpecijalizacija()} className="text-primary font-weight-bold">Specijalizacija</th>
                      <th className="text-primary font-weight-bold">Radno vreme</th>
                      <th  onClick={() => this.sortByOcjena()} className="text-primary font-weight-bold">Prosečna ocena</th>   
                            { (this.state.srchClicked) && <th className="text-primary font-weight-bold">Slobodni termini</th> }                                               
                    </tr>
                  </thead>
                  <tbody>  
                  {this.state.doctors.map(doctor => (
                    <tr key={doctor.id} data-key={doctor.id}>
                        <td>{doctor.user.name + " " + doctor.user.surname}</td>
                        <td>{doctor.type}</td>
                        <td>{doctor.startHr + "h - " + doctor.endHr + "h"}</td>
                        <td>{doctor.rating}</td>   
                        {  (this.state.srchClicked) && <td><Button color="info" onClick = {this.setSelected}>SLOBODNI TERMINI</Button></td>  }                    
                    </tr>
                    ))}
                    
                  </tbody>
                </table>
              </div>
            </div>
          </div>    
    </div>
    </section>  
    
    <section className="bar pt-0" hidden={this.state.hideQuicks} >
    <div id = "pregledi" className="row"> 
    {this.state.quicks.map(quick => (
    <Card style={{ width: "18rem", textAlign: "center"}} key={quick.id}  data-key={quick.id}>
          <CardBody>
            <h4 style={{textAlign: "center"}} className="text-primary font-weight-bold">{quick.checkUpType.name}</h4>
            <p style={{textAlign: "left", marginLeft: "5"}}>
              Datum i vreme: {quick.date + ", " + quick.time + "h"} <br></br>
              Doktor: {quick.medicalWorker.user.name + " " + quick.medicalWorker.user.surname} <br></br>
              Sala za pregled: {quick.room.name} <br></br>
              Cena i popust: {quick.price + " " + quick.discount + "%"} <br></br>                     
            </p>
            <Button className="btn-round" color="info" onClick={this.bookQuick}>ZAKAŽI</Button>
          </CardBody>
        </Card>
         ))}
    </div>
    </section>



    </Container>
    </div>

    </>
    <NotificationContainer/>
    </div>
  );}
}

export default withRouter(ClinicHomePage);
