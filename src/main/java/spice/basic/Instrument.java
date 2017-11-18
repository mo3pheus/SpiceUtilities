
package spice.basic;

import spice.basic.CSPICE;


/**
Class Instrument is used to represent identities of spacecraft
structures or instruments.

<p> This class takes the place of integer ID codes and instrument names
used by subroutine interfaces in SPICELIB and CSPICE.  However,
instrument names and codes are still used to construct Instrument objects:
either a name or NAIF integer code must be supplied in order to construct
an Instrument.

<h3> Version 2.0.0 26-DEC-2016 (NJB)</h3>

Functional change: the getName method now returns a copy of the 
String value used to create the instance, if the instance
was created using a name rather than an ID code. Previously the name
was generated by converting the instance's name field to an integer ID,
then converting the ID back to a name.

<p>The copy constructor now uses the stored IDMap instance's 
deepCopy method, rather than converting the stored instance to
an integer ID and using that to create a new instance. 


<h3> Version 1.0.0 25-AUG-2009 (NJB)</h3>
*/
public class Instrument extends Object
{

   /*
   Instance variables
   */
   private IDMap               instrumentID;


   /*
   Constructors
   */

   /**
   Construct an Instrument from a instrument name.
   */
   public Instrument ( String name )

      throws SpiceException
   {
      //
      // Reject blank or empty instrument names.
      //

      if ( name.equals( "" ) )
      {
         SpiceException exc = SpiceErrorException.create(

            "Instrument",
            "SPICE(EMPTYSTRING)",
            "Input instrument name string was empty." );

         throw ( exc );
      }
      else if ( name.trim().equals( "" ) )
      {
         SpiceException exc = SpiceErrorException.create(

            "Instrument",
            "SPICE(BLANKSTRING)",
            "Input instrument name string was blank." );

         throw ( exc );
      }


      instrumentID = new InstrumentName ( name.trim() );
   }


   /**
   Construct an Instrument from an integer code.
   */
   public Instrument ( int code )

      throws SpiceErrorException
   {
      instrumentID = new InstrumentCode( code );
   }


   /**
   Construct an Instrument from another Instrument.
   */
   public Instrument ( Instrument inst )

      throws SpiceException
   {
      this.instrumentID = inst.instrumentID.deepCopy();
   }


   /*
   Instance methods
   */


   /**
   Test two Instruments for equality.

   <p> The integer codes of the instruments are used for the comparison.
   */
   public boolean equals ( Object obj )
   {
       if (  !( obj instanceof Instrument )  )
       {
          return false;
       }

       //
       // Since this method overrides a method that doesn't
       // throw a SpiceException, this method can't throw
       // a SpiceException either. We'll have to catch that
       // exception if it occurs.
       //
       try
       {
          boolean retVal =  ( (Instrument)obj ).getIDCode() == this.getIDCode();

          return ( retVal );
       }
       catch ( SpiceException e )
       {
          return false;
       }
   }


   /**
   Return a hash code for this Instrument. This method overrides Object's
   hashcode() method.

   <p> Note that this method can't throw a SpiceException.
   */
   public int hashCode()
   {
      /*
      The hashcode value is the hash code of the Integer instrument ID.
      */
      try
      {
         Integer code = new Integer( this.getIDCode() );

         return (  code.hashCode() );
      }
      catch ( SpiceException e )
      {
         return ( 0 );
      }
   }


   /**
   Return NAIF ID code associated with an Instrument.
   */
   public int getIDCode()

      throws SpiceException
   {
      return ( instrumentID.getIDCode() );
   }


   /**
   Return name associated with an Instrument.
   */
   public String getName()

      throws SpiceException
   {
      return ( instrumentID.getName() );
   }


   /**
   Return instrument name in String.  This method overrides Object's
   toString() method.

   Note that this method can't throw a SpiceException.
   */
   public String toString()
   {
      String name;

      try
      {
         name = new String (  this.getName() );
      }
      catch ( SpiceException se )
      {
         //
         // Return the exception's message as the name.
         //
         name = se.getMessage();
      }

      return ( name );
   }

   /**
   Return SCLK ID associated with an Instrument. The returned ID is
   that returned by the SPICE routine CKMETA.
   */
   public SCLK getSCLK()

      throws SpiceException
   {
      int code = CSPICE.ckmeta( this.getIDCode(), "SCLK" );

      return ( new SCLK(code) );
   }
}
