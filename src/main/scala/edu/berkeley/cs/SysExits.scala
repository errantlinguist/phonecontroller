/*
 * Copyright (c) 1987, 1993
 *	The Regents of the University of California.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *	notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *	notice, this list of conditions and the following disclaimer in the
 *	documentation and/or other materials provided with the distribution.
 * 3. All advertising materials mentioning features or use of this software
 *	must display the following acknowledgement:
 *	This product includes software developed by the University of
 *	California, Berkeley and its contributors.
 * 4. Neither the name of the University nor the names of its contributors
 *	may be used to endorse or promote products derived from this software
 *	without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 *	@(#)sysexits.h	8.1 (Berkeley) 6/2/93
 */

/**
  *  SysExits.scala -- Exit status codes for system programs.
  *
  *  <p>Converted from a C header file to a Scala object from a similar Java interface.</p>
  *
  * 	<p>This include file attempts to categorize possible error
  * 	exit statuses for system programs, notably delivermail
  * 	and the Berkeley network.</p>
  *
  * 	<p>Error numbers begin at EX__BASE to reduce the possibility of
  * 	clashing with other exit statuses that random programs may
  * 	already return.  The meaning of the codes is approximately
  * 	as follows:</p>
  *
  * 	<p>EX_USAGE -- The command was used incorrectly, e.g., with
  * 		the wrong number of arguments, a bad flag, a bad
  * 		syntax in a parameter, or whatever.</p>
  * 	<p>EX_DATAERR -- The input data was incorrect in some way.
  * 		This should only be used for user's data & not
  * 		system files.</p>
  * 	<p>EX_NOINPUT -- An input file (not a system file) did not
  * 		exist or was not readable.  This could also include
  * 		errors like "No message" to a mailer (if it cared
  * 		to catch it).</p>
  * 	<p>EX_NOUSER -- The user specified did not exist.  This might
  * 		be used for mail addresses or remote logins.</p>
  * 	<p>EX_NOHOST -- The host specified did not exist.  This is used
  * 		in mail addresses or network requests.</p>
  * 	<p>EX_UNAVAILABLE -- A service is unavailable.  This can occur
  * 		if a support program or file does not exist.  This
  * 		can also be used as a catchall message when something
  * 		you wanted to do doesn't work, but you don't know
  * 		why.</p>
  * 	<p>EX_SOFTWARE -- An internal software error has been detected.
  * 		This should be limited to non-operating system related
  * 		errors as much as possible.</p>
  * 	<p>EX_OSERR -- An operating system error has been detected.</p>
  * 		This is intended to be used for such things as "cannot
  * 		fork", "cannot create pipe", or the like.  It includes
  * 		things like getuid returning a user that does not
  * 		exist in the passwd file.</p>
  * 	<p>EX_OSFILE -- Some system file (e.g., /etc/passwd, /etc/utmp,
  * 		etc.) does not exist, cannot be opened, or has some
  * 		sort of error (e.g., syntax error).</p>
  * 	<p>EX_CANTCREAT -- A (user specified) output file cannot be
  * 		created.</p>
  * 	<p>EX_IOERR -- An error occurred while doing IO on some file.</p>
  * 	<p>EX_TEMPFAIL -- temporary failure, indicating something that
  * 		is not really an error.  In sendmail, this means
  * 		that a mailer (e.g.) could not create a connection,
  * 		and the request should be reattempted later.</p>
  * 	<p>EX_PROTOCOL -- the remote system returned something that
  * 		was "not possible" during a protocol exchange.</p>
  * 	<p>EX_NOPERM -- You did not have sufficient permission to
  * 		perform the operation.  This is not intended for
  * 		file system problems, which should use NOINPUT or
  * 		CANTCREAT, but rather for higher level permissions.</p>
  */

package edu.berkeley.cs

object SysExits {
	/** successful termination */
	val EX_OK = 0

	/** base value for error messages */
	val EX__BASE = 64

	/** command line usage error */
	val EX_USAGE = 64
	/** data format error */
	val EX_DATAERR = 65
	/** cannot open input */
	val EX_NOINPUT = 66
	/** addressee unknown */
	val EX_NOUSER = 67
	/** host name unknown */
	val EX_NOHOST = 68
	/** service unavailable */
	val EX_UNAVAILABLE = 69
	/** internal software error */
	val EX_SOFTWARE = 70
	/** system error (e.g., can't fork) */
	val EX_OSERR = 71
	/** critical OS file missing */
	val EX_OSFILE = 72
	/** can't create (user) output file */
	val EX_CANTCREAT = 73
	/** input/output error */
	val EX_IOERR = 74
	/** temp failure; user is invited to retry */
	val EX_TEMPFAIL = 75
	/** remote error in protocol */
	val EX_PROTOCOL = 76
	/** permission denied */
	val EX_NOPERM = 77
	/** configuration error */
	val EX_CONFIG = 78

	/* maximum listed value */
	val EX__MAX = 78
}
