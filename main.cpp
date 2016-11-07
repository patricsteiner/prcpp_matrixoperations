#include <cstring>
#include "Matrix.h"

void fill(jdouble* a, int l, jdouble val) {
	for (int i = 0; i < l; i++) *(a+i) = val;
}

void swap(jdouble*& a, jdouble*& b) {
	jdouble* tmp = a;
	a = b;
	b = tmp;
}

void multiply(jdouble* pa, jdouble* pb, jdouble* pr, jint ah, jint aw, jint bw) {
	jdouble* ia = pa; // copy pointer, because we don't want to edit original pointer
	jdouble* ir = pr; // dito
	for (int i = 0; i < ah; i++) {
		jdouble* ib = pb; // dito
		for (int j = 0; j < aw; j++) {
			jdouble tmp = *ia++; // save result in tmp to reduce array access (every value is only accessed once)
			for (int k = 0; k < bw; k++) {
				*ir++ += tmp * *ib++; // add intermediate result to right place in result matrix
			}
			ir -= bw; // jump back to the start of the row (we're not done adding the intermediate results)
		}
		ir += bw; // we're done with the row, go to next row
	}
}

JNIEXPORT void JNICALL Java_Matrix_multiplyC(JNIEnv* env, jobject obj, jdoubleArray a, jdoubleArray b, jdoubleArray r, jint ah, jint aw, jint bw) {
	jboolean ca; // tells whether the array was copied or just pinned to memory (not used here because irrelevant).
	jboolean cb; // dito
	jboolean cr; // dito
	jdouble* pa = env->GetDoubleArrayElements(a, &ca); // array is either copied or pinned (doesn't really matter though)
	jdouble* pb = env->GetDoubleArrayElements(b, &cb);
	jdouble* pr = env->GetDoubleArrayElements(r, &cr);

	multiply(pa, pb, pr, ah, aw, bw);

	// mode has no effect if elems is not a copy of the elements in array.
	// 0: copy back the content and free the elems buffer
	// JNI_COMMIT: copy back the content but do not free the elems buffer
	// JNI_ABORT: free the buffer without copying back the possible changes
	env->ReleaseDoubleArrayElements(a, pa, JNI_ABORT); // first two arrays needn't to be copied back
	env->ReleaseDoubleArrayElements(b, pb, JNI_ABORT);
	env->ReleaseDoubleArrayElements(r, pr, 0); // copy array back if it was copied, otherwise unpin
}

JNIEXPORT void JNICALL Java_Matrix_powerC(JNIEnv* env, jobject obj, jdoubleArray m, jdoubleArray r, jint s, jint k) {
	jboolean cm; // tells whether the array was copied or just pinned to memory (not used here because irrelevant).
	jboolean cr; // dito
	jdouble* pm = env->GetDoubleArrayElements(m, &cm); // array is either copied or pinned (doesn't really matter though)
	jdouble* pr = env->GetDoubleArrayElements(r, &cr);
	int len = s*s;
	jdouble* pm2 = new jdouble[len]; // the matrix to be powered
	std::memcpy(pm2, pm, sizeof(jdouble) * len);
	jdouble* pr2 = new jdouble[len]; // the intermediate result matrix
	jdouble* pres = new jdouble[len]; // identity matrix
	int diag = 0;
	for (int i = 0; i < len; i++) {
		if (i == diag) { pres[i] = 1; diag += s + 1;}
		else pres[i] = 0; 
	}
	while (k > 0) {
		if ((k & 1) == 1) {
			fill(pr2, len, 0);
			multiply(pres, pm2, pr2, s, s, s);
			swap(pres, pr2);
		}
		if (k > 1) { // make sure not to square when the result is already set
			fill(pr2, len, 0);
			multiply(pm2, pm2, pr2, s, s, s);
			swap(pm2, pr2);
		}
		k >>= 1;
	}
	std::memcpy(pr, pres, sizeof(jdouble) * len);
	env->ReleaseDoubleArrayElements(m, pm, JNI_ABORT); // first array needn't to be copied back
	env->ReleaseDoubleArrayElements(r, pr, 0); // copy array back if it was copied, otherwise unpin
	delete[] pr2; pr2 = nullptr;
	delete[] pm2; pm2 = nullptr;
	delete[] pres; pres = nullptr;
}

