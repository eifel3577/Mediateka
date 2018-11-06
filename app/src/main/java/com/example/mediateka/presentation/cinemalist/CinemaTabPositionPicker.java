package com.example.mediateka.presentation.cinemalist;




import com.example.mediateka.domain.UseCase;
import com.example.mediateka.domain.UseCaseSubscriber;
import com.example.mediateka.domain.cinemausecases.GetCinemas;
import com.example.mediateka.domain.cinemausecases.GetTopRatedCinemas;
import com.example.mediateka.domain.cinemausecases.GetUpComingCinemas;
import com.example.mediateka.models.model.Cinema;
import com.example.mediateka.presentation.common.CinemaTabSelectorView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CinemaTabPositionPicker {

    /**название таба */
    private String tabPositionName;

    /** Usecase для получения списка фильмов*/
    private final GetCinemas getCinemas;
    /** Usecase для получения списка фильмов по рейтингу (лучшие по рейтингу)*/
    private final GetTopRatedCinemas getTopRatedCinemas;
    /** Usecase для получения ожидаемых фильмов*/
    private final GetUpComingCinemas getUpComingCinemas;
    /**мапа табов хранит стринг(это название нажатого таба,то есть POPULAR,TOP_RATED или UP_COMING) и нажатый таб(значение) */
    private final Map<String , CinemaTab> mapOfTabs = new HashMap<>();
    /**мапа юзкейсов хранит нажатый таб (ключ) и usecase (значение)*/
    private final Map<CinemaTab , UseCase<List<Cinema>>> mapOfUseCases = new HashMap<>();

    /**первая версия конструктора в момент вызова конструктора мапа таб-usecase очищается */
    public CinemaTabPositionPicker() {
        this(null , null , null);
        mapOfUseCases.clear();
    }

    /**вторая версия конструктора, принимает три usecase для разных списков фильмов,с помощью них заполняет мапы
     * табов и юзкейсов */
    public CinemaTabPositionPicker(GetCinemas getCinemas,
                                   GetTopRatedCinemas getTopRatedCinemas,
                                   GetUpComingCinemas getUpComingCinemas) {
        this.getCinemas = getCinemas;
        this.getTopRatedCinemas = getTopRatedCinemas;
        this.getUpComingCinemas = getUpComingCinemas;

        mapOfTabs.put(CinemaTab.POPULAR.name() , CinemaTab.POPULAR);
        mapOfTabs.put(CinemaTab.TOP_RATED.name() , CinemaTab.TOP_RATED);
        mapOfTabs.put(CinemaTab.UP_COMING.name() , CinemaTab.UP_COMING);

        mapOfUseCases.put(CinemaTab.POPULAR , getCinemas);
        mapOfUseCases.put(CinemaTab.TOP_RATED , getTopRatedCinemas);
        mapOfUseCases.put(CinemaTab.UP_COMING , getUpComingCinemas);
    }

    public <T extends CinemaTabSelectorView> void loadCinemaFromCinemaTabName(String tabPositionName ,
                                                                              UseCaseSubscriber<List<Cinema>> subscriber ,
                                                                              T view) {
        if (mapOfUseCases.size() != 0){
            this.tabPositionName = tabPositionName;
            mapOfTabs
                    .get(tabPositionName)
                    .loadCinemaList(mapOfUseCases.get(CinemaTab.valueOf(tabPositionName)) , subscriber , view);
        }
    }

    /**в enum метод values() возвращает массив, содержащий список констант перечисления.
     * в данном случае он возвращает константу по позиции
     * вызывается метод loadCinemaList у соответсвующего enam из CinemaTab
     * @param position позиция нажатого таба
     * @param selectorView вью MainActivity */
    public <T extends CinemaTabSelectorView> void loadCinemaFromCinemaPosition(int position , T selectorView) {
        CinemaTab cinemaTab = CinemaTab.values()[position];
        cinemaTab.loadCinemaList(null , null , selectorView);
    }

    public void dispose(){
        if (tabPositionName != null){
            for (CinemaTab cinemaTab: mapOfTabs.values()){
                cinemaTab.dispose();
            }
        }
    }

    public void setCurrentPage(int currentPage) {
        getCinemas.setCurrentPage(currentPage);
        getTopRatedCinemas.setCurrentPage(currentPage);
        getUpComingCinemas.setCurrentPage(currentPage);
    }
}
