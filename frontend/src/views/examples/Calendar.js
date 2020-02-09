import "../../../node_modules/react-big-calendar/lib/css/react-big-calendar.css"

import React, {Component} from 'react';
import { Calendar, momentLocalizer } from 'react-big-calendar'
import {Button, Row, Col, Table} from 'reactstrap'
import moment from 'moment'
import ExamplesNavbar from 'components/Navbars/ExamplesNavbar.js';
import ProfilePageHeader from 'components/Headers/ProfilePageHeader.js';
import axios from 'axios';
import { NotificationManager } from "react-notifications";

const localizer = momentLocalizer(moment)
const url = 'https://clinical-center-tim31.herokuapp.com/'

class MyCalendar extends Component {
    constructor(props)
    {      
      super(props);
      this.state = {
          events: [],
          event: {}
      }
    }

  componentDidMount(){
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
    let data = 1;
    axios({
      method: 'get',
      url: url + 'checkup/getCheckups/'+data,
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
            sala: checkups[i].room.name + ' ' +  checkups[i].room.number,
            patientId: checkups[i].patient.user.id,
            title: checkups[i].patient.user.name + ' ' + checkups[i].patient.user.surname,
            start: new Date(date[0], Number(date[1]) - 1, date[2], time, 0, 0),
            end: new Date(date[0], Number(date[1]) - 1, date[2], endTime, 0, 0),
            color: color,
            type: checkups[i].type
          }
          temp.push(data);
        }
      }
      this.setState({events: temp})
    },(error)=>{
      console.log(error);
    });
  }

  logoutUser = () => {  
    localStorage.removeItem('ulogovan')
    localStorage.removeItem('role')
    this.props.history.push('/register-page');
  }

    doc = document.documentElement.classList.remove("nav-open");

  handleClickEvent(e){
    let dateTime = String(e.start);
    dateTime = dateTime.split(" ");
    let date = dateTime[1] + ' ' + dateTime[2] + ' ' + dateTime[3];
    let time = dateTime[4];
    let temp = time.split(":");
    let time2 = (Number(temp[0])+1) + ':00:00';
    let data = {
      sala: e.sala,
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

  tooltipRender(e){
    return  e.title
  }

  showDetails(id){
    this.props.history.push('/patient-page/' + id);
  }

  cancelCheckup(id){
    axios({
      method: 'post',
      url:url + 'checkup/cancelCheckup/' + id ,             
    }).then((response)=>{ 
      if (response.status !== 404)      
      NotificationManager.info('Pregled uspjesno otkazan', '');

      let token = localStorage.getItem("ulogovan")
      let AuthStr = 'Bearer '.concat(token);
      let data = 1;
      axios({
        method: 'get',
        url:url + 'checkup/getCheckups/'+data,
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
              sala: checkups[i].room.name + ' ' +  checkups[i].room.number,
              patientId: checkups[i].patient.user.id,
              title: checkups[i].patient.user.name + ' ' + checkups[i].patient.user.surname,
              start: new Date(date[0], Number(date[1]) - 1, date[2], time, 0, 0),
              end: new Date(date[0], Number(date[1]) - 1, date[2], endTime, 0, 0),
              color: color,
              type: checkups[i].type
            }
            temp.push(data);
          }
        }
        this.setState({events: temp})
      },(error)=>{
        console.log(error);
      });
      
    },(error)=>{
      console.log(error);
    });
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

  logoutUser = () => {  
    localStorage.removeItem('ulogovan')
    localStorage.removeItem('role')
    this.props.history.push('/register-page');
  }

  render() {
    return (
      <>
        <ExamplesNavbar recipes={() => this.loadRecipes()}
                      logoutEvent={this.logoutUser}                      
                      pacijentiEvent = {() => this.pacijentiEventShow()}
                      absence = {() => this.setState({showNewAbsence:true})}
                      showProfileEvent = {() => this.props.history.push('/medicalworker-page')}
                      hideNewWorker = {true}
                      hideNewQuick = {true}
                      hideTypeAdmin = {true}
                      hideCodebookAdmin = {true}
                      hideRequestsAdmin = {true}                      
                      hidePatientKlinike = {true}
                      hideCheckupDoctor = {true}
                      hideRoomsAdmin = {true}
                      hideDocsAdmin = {true}
                      hideClinics = {true}
                      hideClinicInfoAdmin = {true}
                      hideAddNewClinic = {true}                      
                      hideVacation = {true}                      
                      sakrij = {true}
                      hideAllQuicksEvent = {true}                      
                      hideKarton = {true}
                      hidePregledi = {true}
                      hideRegisterEvent = {true}
                      hideLoginEvent = {true}
                         />
        <ProfilePageHeader />
        <div>
    
  </div>
  <div>
            <Row>
                <Col md={{ span: 0 }} xs={12}>
                    <h3 align="center" className="border-bottom">Radni kalendar</h3>
                </Col>
            </Row>
            <Row>
                <Col md={{offset: 1 }}  xs={9}>
                  <Calendar 
                    localizer={localizer}
                    events={this.state.events}
                    popup = 'true'
                    views={['day', 'week', 'month']}
                    popupOffset={{x: 30, y: 20}}
                    startAccessor="start"
                    endAccessor="end"
                    style={{height: 700}}
                    onSelectEvent = {(e) => this.handleClickEvent(e)}
                    eventPropGetter = {event => ({
                      style: {backgroundColor : event.color}
                    })}
                    tooltipAccessor = {(e) => this.tooltipRender(e)}
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
                                    <td  colspan = {2} align = "center"><h4>Izabrani događaj</h4></td>
                                </tr>
                                <tr>
                                    <th>Tip</th>
                                    <td align="left">{this.state.event.type}</td>
                                </tr>
                                <tr>
                                    <th>Pacijent</th>
                                    <td align="left">{this.state.event.title}</td>
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
                                <tr>
                                    <th>Sala</th>
                                    <td align="left">{this.state.event.sala}</td>
                                </tr>
                                    
                                    <tr hidden = {this.state.event.type !== 'PREGLED'}>
                                        <td colSpan="2" align="left">
                                            <Button block className="btn-round" color="info"  onClick={(e) => this.showDetails(this.state.event.patientId)}>Više o pacijentu</Button>
                                        </td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                    <td colSpan="2" align="left">
                                            <Button block className="btn-round" color="info"  onClick={(e) => this.cancelCheckup(this.state.event.id)}>Otkaži pregled</Button>
                                        </td>

                                    </tr>

                            </tbody>
                        </Table>
                        </Row>
                    </Col>
            </Row>
        </div>
    </>
  )};
}

export default MyCalendar;