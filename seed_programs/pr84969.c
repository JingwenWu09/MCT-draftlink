#include<stdio.h>
#include<stdlib.h>
#include<math.h>
/* { dg-do run } */
/* { dg-options "-O2 -ftree-loop-distribute-patterns" } */

static void
__attribute__((noipa, noinline))
foo (char **values, int ndim, char *needquotes, int *dims)
{
  int i;
  int j = 0;
  int k = 0;
  char *retval = (char *)__builtin_malloc(1000); 
  char *p = retval;
  char *tmp;

  int indx[111];

	*p++ = '{';
	*p = '\0';

	for (i = 0; i < ndim; i++)
		indx[i] = 0;
	do
	{
		for (i = j; i < ndim - 1; i++){
			*p++ = '{';
			*p = '\0';
		}
		__builtin_strcpy(p, values[k]);
		p += __builtin_strlen(p);
		k++;

		for (i = ndim - 1; i >= 0; i--)
		{
			indx[i] = (indx[i] + 1) % dims[i];
			if (indx[i])
			{
				*p++ = ',';
				*p = '\0';
				break;
			}
			else{
			*p++ = '}';
			*p = '\0';
			}
		}
		j = i;
	} while (j != -1);

	if (__builtin_strcmp (retval, "{{{0,1},{2,3}}}") != 0)
	  __builtin_abort ();
}

int main()
{
  char* array[4] = {"0", "1", "2", "3"};
  char f[] = {0, 0, 0, 0, 0, 0, 0, 0};
  int dims[] = {1, 2, 2};
  foo (array, 3, f, dims);

  return 0;
}
