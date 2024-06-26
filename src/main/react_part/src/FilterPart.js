import { Button } from 'react-bootstrap';
import { useState} from 'react';

function useForceUpdate(){
    const [value, setValue] = useState(0);
    return () => setValue(value => value + 1);
  }

export default function FilterPart(){

    const forceUpdate = useForceUpdate();

    const changeFilterType = (t) => {
        setType(t);
        forceUpdate();
    }

    const [type, setType] = useState(null);

    return(
        <div className="filterblock">
            <Button className="typeButton" onClick = {() => changeFilterType("Day")}>Day</Button>
            <Button className="typeButton" onClick = {() => changeFilterType("Time")}>Time</Button>
            <FilterInput filterType = {type}/>
        </div>
    );
}

export function FilterInput({filterType}){

    const [output, setOutput] = useState(null);

    function submitDayFilter(){
        let days = [];
        if (document.getElementById("monday").checked){
            days.push("M");
        }
        if (document.getElementById("tuesday").checked){
            days.push("T");
        }
        if (document.getElementById("wednesday").checked){
            days.push("W");
        }
        if (document.getElementById("thursday").checked){
            days.push("R");
        }
        if (document.getElementById("friday").checked){
            days.push("F");
        }
        setOutput("Day " + days.join("|"));
    }

    function submitTimeFilter(){
        let st = document.getElementById("startTime").value;
        let et = document.getElementById("endTime").value;
        setOutput("Time " + st + "|" + et);
    }

    if(filterType === "Day"){
        return(
        <div>
            <input type="checkbox" id="monday" name="monday" onClick={submitDayFilter}/>
            <label for="monday">Monday </label>
            <br></br>
            <input type="checkbox" id="tuesday" name="tuesday" onClick={submitDayFilter}/>
            <label for="tuesday">Tuesday </label>
            <br></br>
            <input type="checkbox" id="wednesday" name="wednesday" onClick={submitDayFilter}/>
            <label for="wednesday">Wednesday </label>
            <br></br>
            <input type="checkbox" id="thursday" name="thursday" onClick={submitDayFilter}/>
            <label for="thursday">Thursday </label>
            <br></br>
            <input type="checkbox" id="friday" name="friday" onClick={submitDayFilter}/>
            <label for="friday">Friday </label>
            <p id="filters">{output}</p>
        </div>
        );
    }
    if(filterType === "Time"){
        return(
            <div>
                <p>Start Time: </p>
                <input type="text" id="startTime" name="startTime"/>
                <p>End Time: </p>
                <input type="text" id="endTime" name="endTime" onChange={submitTimeFilter}/>
                <p id="filters">{output}</p>
            </div>
        )
    }
}

