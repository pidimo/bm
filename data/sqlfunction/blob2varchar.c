#include <mi.h>
#define MAXBUFSIZE 2147483647

/*that routine*/
mi_lvarchar *blob2varchar(loHandle, fParam)
MI_LO_HANDLE *loHandle; 
MI_FPARAM *fParam; 
{ 
	MI_LO_FD lofd; 
	char *buf;
	mi_integer buflen = 0; 
	mi_int8 offset; 
	mi_integer numbytes;
	/*result*/
	mi_lvarchar *resultLv;

	/*LO status*/
	MI_LO_STAT *LO_stat = NULL; /* DataBlade API allocates */
	mi_integer err;
	mi_int8 sLO_Size;

	char hasError = 0;


	/* determine if the first argument is NULL */
	if ( mi_fp_argisnull(fParam, 0) == MI_TRUE ) 
	{
		mi_db_error_raise(NULL, MI_MESSAGE, "Addition to a NULL value is undefined.\n"); 
		
		/* return an SQL NULL value */
		mi_fp_setreturnisnull(fParam, 0, MI_TRUE); 
		/* the argument to this "return" statement is ignored by the 
		* database server because the previous call to the 
		* mi_fp_setreturnisnull( ) function has set the return value 
		* to NULL */ 

		return NULL;

	} else {

		/* Use the LO handle to identify the smart large object 
		* to open and get an LO file descriptor. */ 
		lofd = mi_lo_open(NULL, loHandle, MI_LO_RDONLY); 

		if ( lofd == MI_ERROR ){ 
			//file descriptor error
			mi_db_error_raise(NULL, MI_MESSAGE, "mi_lo_open() failed.\n"); 
			hasError = 1;

		} else {

			/* Allocate LO-specification structure and get status 
			* information for the opened smart large object */ 
			if ( mi_lo_stat(NULL, lofd, &LO_stat) == MI_OK ){

				/* get size in bytes for this smart large object */ 
				if(mi_lo_stat_size(LO_stat,&sLO_Size) == MI_OK){

					if(ifx_int8tolong(&sLO_Size,&buflen) == 0){

						/*allocate buffer length*/
						if(buflen <= MAXBUFSIZE){
							buf = (char *)mi_zalloc(buflen+1);
						} else {
							buf = (char *)mi_zalloc(MAXBUFSIZE);
						}

						if(buf != NULL){
							/* Use the LO file descriptor to read the data of the 
							* smart large object. */ 
							ifx_int8cvint(0, &offset); 
							
							///numbytes = mi_lo_readwithseek(NULL, lofd, buf, buflen, &offset, MI_LO_SEEK_CUR);
							numbytes = mi_lo_read(NULL, lofd, buf, buflen);

							if ( numbytes != MI_ERROR) {

								//get result
								resultLv = mi_string_to_lvarchar(buf);
								if(resultLv == NULL){
									mi_db_error_raise(NULL, MI_MESSAGE, "result failed.\n"); 
									hasError = 1;
								}
								
							} else {
								//LO read error
								mi_db_error_raise(NULL, MI_MESSAGE, "mi_lo_readwithseek() failed.\n"); 
								hasError = 1;
							}

						} else {
							//buffer error
							mi_db_error_raise(NULL, MI_MESSAGE, "buffer not allocated....\n"); 
							hasError = 1;
						}

					} else {
						//convert int8 to int failed
						mi_db_error_raise(NULL, MI_MESSAGE, "convert int8 to int failed...\n"); 
						hasError = 1;
					}

				} else {
					//get LO size failed
					mi_db_error_raise(NULL, MI_MESSAGE, "get LO size failed...\n"); 
					hasError = 1;
				}

				/* free the LO-status structure */
				err = mi_lo_stat_free(NULL, LO_stat); 
			} else { 
				/* handle error LO status*/ 
				mi_db_error_raise(NULL, MI_MESSAGE, "mi_lo_stat() failed.\n"); 
				hasError = 1;
			}
		}

		/* Close the smart large object */ 
		mi_lo_close(NULL,lofd);

		/*free buffer*/
		mi_free(buf);

		if(hasError){
			mi_fp_setreturnisnull(fParam, 0, MI_TRUE); 
		}

		return resultLv;
	}
}
