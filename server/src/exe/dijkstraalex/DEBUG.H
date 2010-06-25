#include <stdio.h>
#include <string.h>
#include <stdlib.h>
/*#include <opcrt.h>
#include <opbrowse.h>
#include <opcmd.h>*/


typedef struct typelistrec
   {char name[30];
    struct typelistrec *nexttype;
    struct typelistrec *nextfield;
   } typelist;

typedef struct dummypointertype
   {void *dptr;} dummypointer;


FILE *fout;

struct typelistrec *firstptr;

void dotab(int tabsize)
 {int i;
  tabsize*=5;
  for (i=0; i<tabsize; ++i)
   fprintf(fout,"%s"," ");
  }

void *dodraw(void *vptr, char ptrtype[], int level)
 {char instr[80];
  int done=0;
  int dummyint;
  int *iptr;
  float *fptr;
  char *cptr;
  void *vsaveptr;
  dummypointer *dumptr;
  typelist *ptr;

  if (vptr==NULL) return(0);
  else
  {


  ptr=firstptr;
  while (!done && (ptr != NULL))
   {if (strcmp((*ptr).name,ptrtype)==0) done=1;
    else ptr=(*ptr).nexttype;
    }
    if (!done)
    {return(0);}
    else do
     {ptr=(*ptr).nextfield;
      if (ptr == NULL)
       {return(vptr);}
      else if (strcmp((*ptr).name,"int")==0)
       {iptr=(int *)vptr;
	dotab(level);
	fprintf(fout, "int %d\n", *iptr);
	iptr++;
	vptr=iptr;}
      else if (strcmp((*ptr).name,"char")==0)
       {cptr=(char *)vptr;
	dotab(level);
	fprintf(fout,"char %c\n", *cptr);
	cptr++;
	vptr=iptr;}
      else if (strcmp((*ptr).name,"float")==0)
       {fptr=(float *)vptr;
	dotab(level);
	fprintf(fout,"float %f\n", *fptr);
	fptr++;
	vptr=fptr;}
       else if (strcmp((*ptr).name,"long")==0)
	{long *lptr;
	 lptr=(long *)vptr;
	 dotab(level);
	 fprintf(fout,"long %d\n", *lptr);
	 lptr++;
	 vptr=lptr;}
       else if (strcmp((*ptr).name,"double")==0)
       {double *dptr;
	dptr=(double *)vptr;
	dotab(level);
	fprintf(fout,"float %f\n", *dptr);
	dptr++;
	vptr=dptr;}
      else if (strcmp((*ptr).name,"long double")==0)
       {long double *dptr;
	dptr=(long double *)vptr;
	dotab(level);
	fprintf(fout,"float %f\n", *dptr);
	dptr++;
	vptr=fptr;}

      else if (strcmp((*ptr).name,"array")==0)
       {int maxind[5],currind[5],numind,i,j,p;
	int found=0;
	ptr=(*ptr).nextfield;
	sscanf((*ptr).name,"%d",&numind);
	for (i=0;i<numind;++i)
	 {ptr=(*ptr).nextfield;
	  sscanf((*ptr).name,"%d", &maxind[i]);
	  currind[i]=0;

	 }
	 p=numind-1;
	 ptr=(*ptr).nextfield;
	 do {
	     found=0;
	     dotab(level);
	     for (i=0;i<numind;++i)
	      fprintf(fout, "%d ",currind[i]);

	      vptr=dodraw(vptr,(*ptr).name,level);
	       {for (i=p;i>=0;--i)
		 if (currind[i]<(maxind[i]-1))
		   {for (j=i+1;j<numind;++j)
		      currind[j]=0;
		    currind[i]+=1;
		    found=1;
		    break;}
		}
	     } while (found);
       }
      else
       {vsaveptr=vptr;
	dumptr=(dummypointer *)vptr;
	vptr=(*dumptr).dptr;
	if (vptr != NULL)
	{
	 dotab(level+1);
	 fprintf(fout,"%s\n",(*ptr).name);
	}
	dodraw(vptr,(*ptr).name,level+1);
	vptr=vsaveptr;
	cptr=(char *)vptr;
	cptr+=sizeof(dummypointer);
	vptr=cptr;
       }
     } while (ptr != NULL);

   return(vptr);
  }
  }

void draw(void *vptr, char ptrtype[], char filename[])
 {FILE *fin;
  int end;
  struct typelistrec *ptr,*top;
  char instr[80];

  fin=fopen(filename,"r");
  firstptr = (typelist *) malloc (sizeof (typelist));
  end=fscanf (fin,"%s",&instr);
  ptr=firstptr;
  while (end != EOF)
   {if (strcmp(instr,"type")==0)
     { end=fscanf(fin,"%s",&instr);
       top=ptr;
      while ((strcmp(instr, "type") != 0) && (end != EOF))
      {

       (*ptr).nexttype=NULL;
       strcpy((*ptr).name, instr);
       end=fscanf(fin,"%s",&instr);
       if ((strcmp(instr,"type") !=0) && (end != EOF))
	{(*ptr).nextfield = (typelist *) malloc (sizeof (typelist));
	ptr=(*ptr).nextfield;
	}
       else (*ptr).nextfield=NULL;
      }
     if (end != EOF)
      {(*top).nexttype= (typelist *) malloc (sizeof (typelist));
       ptr=(*top).nexttype;
      }
     }
    }
  fclose(fin);
  fout=fopen("t.t","w");
  dodraw(vptr,ptrtype,1);
  fclose(fout);
  /*
  {Browser B;
   word Status;
   boolean Finished;
  OPBrowseInit ();
  B.InitCustom(2,3, ScreenWidth, ScreenHeight, DefaultColorSet, DefWindowOptions,
	       coreleft ()-10000);
  B.OpenFile("debug.dat");
  Status=B.GetLastError();
  if (Status != 0) {cprintf ("Failed. Status = %u\r\n", Status);
		    return;}
  B.SetStatusFunc(BrowseStatus);
  Finished=FALSE;
  do {
      B.Process ();
      switch (B.GetLastCommand()) {
	 case ccQuit:
	 case ccError:
	   Finished=TRUE;
	   break;
      };
     } while (!Finished);
     B.Erase ();
     B.Done();
     ClrScr();  } */
 }
 /* sample info file
  type
  node
  int
  float
  array
  2 2 2
  int
  node
  type
  int
  int

  Corresponds to Struct
   struct node
   {int x;
    float y;
    int arr[2][2];
    struct node *next}  */

