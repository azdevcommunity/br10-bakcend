package fib.br10.utility;

import fib.br10.core.utility.RequestContextEnum;
import fib.br10.core.utility.RequestContext;
import fib.br10.entity.specialist.Speciality;
import fib.br10.enumeration.LangEnum;

public class SpecialityUtil {

    public static String getSpecialityName(Speciality speciality) {
        LangEnum currentLang = LangEnum.fromValue(RequestContext.get(RequestContextEnum.LANG, String.class));
        return switch (currentLang) {
            case EN -> speciality.getName_en();
            case RU -> speciality.getName_ru();
            case AZ -> speciality.getName();
        };
    }
}
