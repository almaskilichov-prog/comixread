package kz.almas.comixread.interfaces;

import kz.almas.comixread.classes.ObservableScrollView;

public interface ScrollViewListener {
    void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);
}
