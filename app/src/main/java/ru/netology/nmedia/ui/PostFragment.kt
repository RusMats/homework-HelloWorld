package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.PostContentFragmentBinding
import ru.netology.nmedia.databinding.PostFragmentBinding
import ru.netology.nmedia.viewModel.PostViewModel

class PostFragment : Fragment() {

    private val viewModel by viewModels<PostViewModel>()
    private val args by navArgs<PostFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = PostFragmentBinding.inflate(
        layoutInflater, container, false
    ).also { binding->
        val adapter = PostsAdapter(viewModel)
        binding.postCard.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts.filter { it.id == args.postId })
        }

        setFragmentResultListener(
            requestKey = PostContentFragment.REQUEST_KEY
        ) { requestKey, bundle ->
            if (requestKey != PostContentFragment.REQUEST_KEY) return@setFragmentResultListener
            val newPostContent = bundle.getString(
                PostContentFragment.RESULT_KEY
            ) ?: return@setFragmentResultListener
            viewModel.onSaveButtonClick(newPostContent)
        }

        viewModel.navigateToPostContentScreenEvent.observe(this) { initialContent ->
            val direction = PostFragmentDirections.fromPostToPostContentFragment(initialContent)
            findNavController().navigate(direction)
        }
    }.root

}