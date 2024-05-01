import React, { useState, useEffect } from 'react';
import './Home.css';

function Home() {
    const [course, setCourse] = useState('');

    useEffect(() => {
        async function fetchData() {
            const courseData = await getClasses();
            setCourse(courseData);
        }
        fetchData();
    }, []);

    return (
        <div>
            <h1>{course}</h1>
        </div>
    );
}

async function getClasses(){
    const response = await fetch('http://localhost:7979/search/comp');
    const content = await response.json();
    const course = JSON.parse(JSON.stringify(content))[0].courseCode
    return course;
}

function wait(){
    const course = "My Course";
        getClasses().then(function(results){
            const course = results;
            console.log(course);
            return course;
        });
}

function validateJSON(response) {
    if (response.ok) {
        return response.json;
    } else {
        return Promise.reject(response);
    }
}

export default Home;
