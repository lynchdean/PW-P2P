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

The main point that I took from the meeting with Brian was that my project had plenty of breadth, but said I needed to ensure that I had good depth in one area such as the encryption or synchronisation. 
I will meet again with Brian next week.

## Blog Entry 3 - Wed 24 October:
Today I had my proposal presentation. 
My two assesors were Andrew McCarren and Liam Tuohey.
Neither Liam or Andrew use a password manager, so it was a little harder to explain to them why my password manager was different from the ones available on the market currently.
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
After speaking with Brain at my weekly supervision meeting, we decided that enforcing two factor authentication for every user would be a good solution to this problem.

## Blog Entry 6 - Wed 14 November:
This week I continued working on my functional specification. 
In the process of working on this I discovered research papers about password database formats and usage of password managers in enterprise.

Brian was worried that I hadn't been working on writing code on the project, as he feels that this would keep the project fresh in my mind. 
So next week I hope to get started on implementing the project as well as making more progress on the functional specification.

## Blog Entry 7 - Wed 21 November:
Our functional specification deadline was pushed back due to overlapping of assignment deadlines from other modules. 
I was quite happy about this as it would give more time for me to work on implementing the project alongside working on the functional specification.

I also discovered a Java library called KeePassJava2 that should allow me to use the open source and very commonly used KDBX format with my password manager. 
This could potentially save me time that would be spent trying to create my own sort of implementation of a password database file.
More importantly, this could save me the potential security pitfalls I might introduce trying to implement my own database format.

## Blog Entry 8 - Wed 28 November:
This week I finished my functional specification ahead of the deadline this coming Friday. 
In the little time I had to work on my implementation, I used it to continue work on the reading and writing of the database files, as well as writing unit tests to cover it.

## Blog entry 9 - Wed 05 December:
This week I started working on the GUI for my project.
I stated in my functional specification that I would be using The Swing library for my GUI, but after using both Swing and JavaFX, I have to decided to use JavaFX instead, as I found it more modern and full featured.

## Blog entry 10 - Wed 13th December:
No meeting with Brian this week, as there would not be much progress made within a week with three assessment deadlines upcoming in week 12.
I'm continuing to work on the GUI for my project.
As I've never used JavaFX before, I have dedicated a any of my time towards practicing and learning the framework.

## Blog Entry 11 - Mon 21st January:
Since the last blog I have had my Christmas Exams, which means most of my time was dedicated to studying for them.
Whenever I needed a short break from studying, I continued working away at the GUI for my application.

## Blog Entry 12 - Mon 28th January:
This week I decided to rewrite most of the code for my GUI, as the tutorial that I first followed did not anticipate that I would be implementing file input.
While the application seemed to work fine after implementing the file chooser, the application always threw a "ConcurrentModificationError".
I tried using some of the fixes found online for this error, but I had no success.
After spending a considerable amount of time trying to fix this, I made the decision to rewrite most of my GUI code, using a tutorial that takes the file chooser into account.
This was a success and I no longer get the "ConcurrentModificationError".

## Blog Entry 13 - Mon 4th February:
Last week I finished writing a basic GUI for the file selection and credentials input sections of my application.
I now plan on working on the functionality of reading in a .KDBX file and displaying it in a GUI.

## Blog Entry 14 - Thurs 14th February:
I have now implemented some very basic functions such as loading and saving the .kdbx file, and have written Spock tests to cover these operations.
I have also set up a GitLab CI pipeline to automatically run the unit tests every time I push code to the repo.
One problem I had with implementing the loading and saving operations was that they were inconsistently taking a long time to execute.
After searching online for a solution, I found an open issue in the KeepassJava2 library that I'm using describing the exact same issue.
It turns out that some UNIX systems use /dev/random for random number generation, which ["can potentially block the WebLogic SIP Server process because on some operating systems /dev/random waits for a certain amount of "noise" to be generated on the host machine before returning a result".](https://docs.oracle.com/cd/E13209_01/wlcp/wlss30/configwlss/jvmrand.html)
It was quite straightforward to fix this issue on my own machine, but was also causing problems in my CI pipeline.
Since the execution time was long and inconsistent, this issue would sometimes cause the pipeline to run over the 10 minute timeout.
I explored a couple of different ways to solve this, like a specialised docker container, but found a simple solution to the problem by creating a symbolic link from /dev/random to /dev/urandom when the pipeline in the setup script for the pipeline.
