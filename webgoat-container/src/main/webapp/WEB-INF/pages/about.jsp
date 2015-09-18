<%@ page contentType="text/html; charset=ISO-8859-1" language="java"
         errorPage=""%>

<!-- This modal content is included into the main_new.jsp -->

<div class="modal-content">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3 class="modal-title" id="myModalLabel">About WebGoat - Provided by the OWASP Foundation</h3>
    </div>
    <div class="modal-body modal-scroll">
        <p>Thanks for hacking The Goat!</p> 
        <p>WebGoat is a demonstration of common web application flaws. The
            associated exercises are intended to provide hands-on experience with
            techniques aimed at demonstrating and testing application penetration.
        </p>
        <p>From the entire WebGoat team, we appreciate your interest and efforts
            in making applications not just better, but safer and more secure for
            everyone. We, as well as our sacrificial goat, thank you.</p>
        <p>
            Version: ${version},&nbsp;Build: ${build}
        </p>

        <div class="row">
            <div class="col-md-6">
                <p>Contact us:
                <ul>
                    <li>WebGoat mailing list: ${emailList}</li>
                    <li>Bruce Mayhew:  ${contactEmail}</li>
                </ul>
                </p>
            </div>
        </div>       
        <div class="row">
            <div class="col-md-6">
                <p>WebGoat Authors
                <ul>
                    <li>Bruce Mayhew   (Author & Project Lead)</li>
                    <li>Jeff Williams  (Author & Original Idea)</li>
                    <li>Jason White    (Architect)</li>
                    <li>Nanne Baars    (Plugin Architecture)</li>
                    <li>Richard Lawson (Architect)</li>
                </ul>
                </p>
            </div>
            <div class="col-md-6">
                <p>Active Contributors
                <ul>
                    <li>Nanne Baars   (Developer)</li>
                    <li>Jason White   (Developer)</li>
                    <li>Doug Morato   (Developer & CI)</li>
                    <li>Jeff Wayman   (Docs)</li>
                    <li>Bruce Mayhew  (Developer)</li>
                    <li>Michael Dever (Developer)</li>
                </ul>
                </p>
            </div>
        </div>
        <div class="row">
            <div class="col-md-6">
                <p>WebGoat Design Team (Active)
                <ul>
                    <li>Nanne Baars    (Plugin Architecture)</li>
                    <li>Bruce Mayhew   (Goat Herder)</li>
                    <li>Jeff Wayman    (Website and Docs)</li>
                    <li>Jason White    (User Interface)</li>
                </ul>
                </p><br></br>
                <p>Corporate Sponsorship - Companies that have donated significant time to WebGoat development
                <ul>
                    <li>Aspect Security</li>
                    <li>Ounce Labs</li>
                </ul>
                </p><br></br>
                <p>Did we miss you? Our sincere apologies, as we know there have
                    been many contributors over the years. If your name does not
                    appear in any of the lists above, please send us a note. We'll
                    get you added with no further sacrifices required.</p>
            </div>
            <div class="col-md-6">
                <p>Past Contributors
                <ul>
                    <li>Dave Cowden (Everything)</li>
                    <li>Richard Lawson (Service Layer)</li>
                    <li>Keith Gasser (Survey/Security)</li>
                    <li>Devin Mayhew (Setup/Admin)</li>
                    <li>Li Simon (Developer)</li>
                    <li>Ali Looney (UI Design)</li>
                    <li>David Anderson (Developer/Design)</li>
                    <li>Christopher Blum (Lessons)</li>
                    <li>Laurence Casey (Graphics)</li>
                    <li>Brian Ciomei (Bug fixes)</li>
                    <li>Rogan Dawes (Lessons)</li>
                    <li>Erwin Geirnaert (Solutions)</li>
                    <li>Aung Knant (Documentation)</li>
                    <li>Ryan Knell (Lessons)</li>
                    <li>Christine Koppeit (Build)</li>
                    <li>Sherif Kousa (Lessons/Documentation)</li>
                    <li>Reto Lippuner (Lessons)</li>
                    <li>PartNet (Lessons)</li>
                    <li>Yiannis Pavlosoglou (Lessons)</li>
                    <li>Eric Sheridan (Lessons)</li>
                    <li>Alex Smolen (Lessons)</li>
                    <li>Chuck Willis (Lessons)</li>
                    <li>Marcel Wirth (Lessons)</li>
                </ul>
                </p>
            </div>
        </div>
    </div>
    <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
    </div>
</div>
