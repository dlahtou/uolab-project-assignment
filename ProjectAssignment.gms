* This solution does not include the following parameters/variables
* ---Extraordinary group sizes
* ---Student availability times


Sets
         s students
         p projects          ;

Parameters
         groupsizemin minimum group size  /3/
         groupsizemax maximum groups size  /4/
         studentprefscore(s,p) point score of student preferences
         extraordinarygrouplimit maximum number of extraordinary groups /1/ ;

$if not set dbIn $abort 'no file name for indb1'
$gdxin %dbIn%
$load s p studentprefscore
$gdxin

Binary Variables
         x(s,p) assignment of student to project
         a(p) approval of project
         e(p) extraordinary project approval ;

Variable
         z total score              ;

Equations
         scoring         define total score
         oneprojectperstudent(s) limit students to one project
         studentisinpreferredproject(s) ensure student assigned to a project in their preference ranking
         projectmeetsmin(p)      ensure project is at least groupsizemin
         projectmeetsmax(p)      ensure project is under groupsizemax
         unapprovedprojectsgetzero(p) projects without approval get a max size of 0
         maxnumberextraordinarygroups  sets cap on groups with extraordinary sizes;

* Objective Function
scoring .. z =e= sum((s,p), x(s,p)*studentprefscore(s,p));

** Make sure student is assigned to exactly one project
oneprojectperstudent(s) .. sum(p, x(s,p)) =e= 1;

** Make sure student is assigned to a ranked project
studentisinpreferredproject(s) .. sum(p, x(s,p)*studentprefscore(s,p)) =g= 1;

* Ensure group sizes are within approved limits
*projectmeetsmin(p) .. sum(s, x(s,p)) =g= groupsizemin -300*(1-a(p)) ;
     projectmeetsmin(p) .. sum(s, x(s,p)) =g= groupsizemin -300*(1-a(p)) - e(p) ;
*projectmeetsmax(p) .. sum(s, x(s,p)) =l= groupsizemax ;
     projectmeetsmax(p) .. sum(s, x(s,p)) =l= groupsizemax + e(p);

     maxnumberextraordinarygroups .. sum(p, e(p)) =l= extraordinarygrouplimit;

* Ensure only approved groups contain students
unapprovedprojectsgetzero(p) .. sum(s, x(s,p)) =l= 300*a(p)  ;

Model assignment /all/;

Solve assignment using mip maximizing z ;

Display  x.l, z.l;

