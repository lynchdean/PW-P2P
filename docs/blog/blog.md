# Blog: SyncSafe

**Dean Lynch**

## Blog Entry 1 - Fri 11 October:

Last week I organised a meeting with Brian Stone to have a talk about my proposed project idea. 
Brian liked the idea and agreed to be my project supervisor. 

Brian confirmed himself as my project supervisor and completed my project proposal ahead of the deadline today.

## Blog Entry 2 - Wed 18 October:

This week I had my first official meeting with Brian. 
We have organised to meet up every Wednesday at 12 with the other students that Brian is supervising.

Tom was also present at the meeting. 
It was very interesting to hear about Toms project which involves comparing images and determining how similar they are.

The main point that I took from the meeting with Brian was that my project had plenty of breadth, but said I needed to 
ensure that I had good depth in one area such as the encryption or synchronisation. 
I will meet again with Brian next week.

## Blog Entry 3 - Wed 24 October:
Today I had my proposal presentation. 
My two assessors were Andrew McCarren and Liam Tuohey.
Neither Liam or Andrew use a password manager, so it was a little harder to explain to them why my password manager was 
different from the ones available on the market currently.
Both assessors seemed to be satisfied with my proposal and it was approved.

The one area Andrew was worried about was access to the database. 
He wasn't comfortable with only a single password protecting the entire database.
He would like if some sort of two-factor authentication is enforced by the application.

## Blog Entry 4 - Wed 31 October:
This week I started working on my functional specification. 
I have another 3 assignments ongoing at the moment, so it's quite difficult to find time for the project.

## Blog Entry 5 - Wed 07 November:
This week I continued to work on other assignments and made good progress in particular on the Compiler construction assignment.
I was able to find some time to make a little more progress on the functional specification.

I also spent some time researching how other password managers handle the concern that Andrew McCarren had in my project proposal.
After speaking with Brain at my weekly supervision meeting, we decided that enforcing two factor authentication for every user would 
be a good solution to this problem.

## Blog Entry 6 - Wed 14 November:
This week I continued working on my functional specification. 
In the process of working on this I discovered research papers about password database formats and usage of password managers in enterprise.

Brian was worried that I hadn't been working on writing code on the project, as he feels that this would keep the project fresh in my mind. 
So next week I hope to get started on implementing the project as well as making more progress on the functional specification.

## Blog Entry 7 - Wed 21 November:
Our functional specification deadline was pushed back due to overlapping of assignment deadlines from other modules. 
I was quite happy about this as it would give more time for me to work on implementing the project alongside working on the 
functional specification.

I also discovered a Java library called KeePassJava2 that should allow me to use the open source and very commonly used 
KDBX format with my password manager. 
This could potentially save me time that would be spent trying to create my own sort of implementation of a password database file.
More importantly, this could save me the potential security pitfalls I might introduce trying to implement my own database format.

## Blog Entry 8 - Wed 28 November:
This week I finished my functional specification ahead of the deadline this coming Friday. 
In the little time I had to work on my implementation, I used it to continue work on the reading and writing of the database files, 
as well as writing unit tests to cover it.

## Blog entry 9 - Wed 05 December:
This week I started working on the GUI for my project.
I stated in my functional specification that I would be using The Swing library for my GUI, but after using both Swing and JavaFX, 
I have to decided to use JavaFX instead, as I found it more modern and full featured.

## Blog entry 10 - Wed 13th December:
No meeting with Brian this week, as there would not be much progress made within a week with three assessment deadlines upcoming in week 12.
I'm continuing to work on the GUI for my project.
As I've never used JavaFX before, I have dedicated a any of my time towards practicing and learning the framework.

## Blog Entry 11 - Mon 21st January:
Since the last blog I have had my Christmas Exams, which means most of my time was dedicated to studying for them.
Whenever I needed a short break from studying, I continued working away at the GUI for my application.

## Blog Entry 12 - Mon 28th January:
This week I decided to rewrite most of the code for my GUI, as the tutorial that I first followed did not anticipate that 
I would be implementing file input.
While the application seemed to work fine after implementing the file chooser, the application always threw a "ConcurrentModificationError".
I tried using some of the fixes found online for this error, but I had no success.
After spending a considerable amount of time trying to fix this, I made the decision to rewrite most of my GUI code,
 using a tutorial that takes the file chooser into account.
This was a success and I no longer get the "ConcurrentModificationError".

## Blog Entry 13 - Mon 4th February:
Last week I finished writing a basic GUI for the file selection and credentials input sections of my application.
I now plan on working on the functionality of reading in a .KDBX file and displaying it in a GUI.

## Blog Entry 14 - Thurs 14th February:
I have now implemented some very basic functions such as loading and saving the .kdbx file, and have written Spock tests 
to cover these operations.
I have also set up a GitLab CI pipeline to automatically run the unit tests every time I push code to the repo.
One problem I had with implementing the loading and saving operations was that they were inconsistently taking a long time to execute.
After searching online for a solution, I found an open issue in the KeepassJava2 library that I'm using describing the exact same issue.
It turns out that some UNIX systems use /dev/random for random number generation, which 
["can potentially block the WebLogic SIP Server process because on some operating systems /dev/random waits for a certain amount of "noise" to be generated on the host machine before returning a result".]
(https://docs.oracle.com/cd/E13209_01/wlcp/wlss30/configwlss/jvmrand.html)
It was quite straightforward to fix this issue on my own machine, but was also causing problems in my CI pipeline.
Since the execution time was long and inconsistent, this issue would sometimes cause the pipeline to run over the 10 minute timeout.
I explored a couple of different ways to solve this, like a specialised docker container, but found a simple solution to the problem 
by creating a symbolic link from /dev/random to /dev/urandom when the pipeline in the setup script for the pipeline.

## Blog Entry 15 - Thurs 21st February:
After using the solution to the /dev/random problem mentioned in the previous blog for over a weeks now, 
I'm happy with the solution as the times operations take to complete have not once gone back to taking minutes to complete!
I've now started working on the 'homepage' of the application, where the user will be able to view, manage and save their passwords.

I've also discussed GDPR and ethics requirements with Brian, 
and we have decided that it is necessary for me to complete an ethics notification form so that user-testing can be carried 
out to get feedback on the usability of the GUI of the application.

## Blog Entry 16 - Thurs 21st March:
Since the last meeting and blog, I've completed the ethics application and have been approved by two members of the Ethics Committee.
Brian has been unwell for the past couple of weeks, so I have not had a meeting with him since the 21st of February, 
but I have kept him up to date with the progress of my project through via email.

I have since added the main features for the homepage of the GUI. This includes the 'tree view' of the folder groups contained 
inside of the database. 
This was a lot more work than I expected it to be!
I had to traverse the database recursively when the application starts up in order to generate this graphical representation 
of the folder structure.
The graphical tree is also quite limited in what can be attached to each node assigned to it. It can only be assigned a title 
and icon to be displayed.

This was a problem because I need to be able to display each of a folders password entries when a user selects the corresponding 
node in the tree, and because of the limitations of what can be assigned to the graphical node, there was no way to store which folder inside 
the actual database the node was actually representing.

To solve this problem, I had to build up a path string each time a graphical node was clicked.
This would give me a way of finding the exact folder inside the password database.
One consequence of this however was that it forced me to prevent users from creating two folders with the same title inside the same folder.
This is already standard practice in most operating systems, so it isn't a just a lazy workaround my problem!

Once this was solved, when a node was clicked on the tree, 
I was able to display each of the password entries associated with that specific folder in a table. This covers the two 
main features of the homepage GUI,  
and the next major graphical feature needed to be added is a full view of a password entries details.

## Blog Entry 17 - Thurs 28th March:
Since last week I've added the ability to view each of the details inside a password entry.
Alongside this, I've also added the abilities to save and edit the entries.

I've also made overall improvements to the GUI to make it cleaner and more dynamic.
I've now started working on the synchronisation aspect of the application.

## Blog Entry 18 - Thurs 4th April:
I have now written the components needed for two instances of the application to connect with each other.
I now plan on integrating this with the GUI of the application.

I have also made the decision not to include 2-factor authentication (2FA) in the application.
This was a feature I would have really liked to include in the application,
as it would have added another layer of security and given the user a greater piece of mind that their passwords are
protected from being compromised.
This was highlighted in the project proposal stage, and I was asked to include 2FA for a greater level of security,
but unfortunately I do not have the sufficient time to implement this feature before the deadline without taking time away
from other more significant features.

## Blog Entry 19 - Thurs 11th April:
Following on from the progress from last week, I've now integrated the network features into the GUI of the application,
although it still needs further improvements to be more straightforward and user friendly.
While making these usability improvements to this area, I also plan on making usability improvements in other areas, such as
allowing users to copy their entry details to their application with a click of a button, allowing users to toggle the visibility
of sensitive password information, and built in password generation.

## Blog Entry 20 - Thurs 18th April:
This week I had my last scheduled meeting with Brian, although we do plan on meeting again before the project demonstrations and EXPO
take place to ensure everything goes smoothly!

I have now added the 3 features mentioned in the previous blog entry, and just have a few more finishing touches to make to the application
for me to consider it in a complete and presentable condition.
That is not to say that I'm finished working on the application as I will continue adding to it right up until the deadline
as there is plenty more features that I would love to include.

Lectures are now coming to an end and exams are hastily approaching, meaning that exam study must take a priority for the next few weeks.

## Blog Entry 21 - Sun 19th May:
Today is the final day before the big project deadline.
Due to exams for the past few weeks and the following mad rush to work on my project before the deadline,
I have neglected my blog! 

I've now finished working on the project and I'm preparing for the final submission of the last couple of 
documents. While I'm very happy with the resulting application, there's so much more I would have loved to 
include. This project has been a great learning experience and I'll be a sad to see it come to and end.

After today I will continue preparations for my final demonstration of the project. I hope you have enjoyed 
reading this blog, and hope it gives you a better understanding of the different stages of development I went through 
over the course of working on this project.

 